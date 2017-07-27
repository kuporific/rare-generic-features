package com.implementsblog.gencon;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.google.common.collect.Streams;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by justinrogers on 7/10/17.
 */
public class Main {

    private final List<Tuple2<Path, List<ConstructorDeclaration>>> allGenericDeclarationsOnConstructors;
    private final List<Tuple2<Path, List<TypeParameter>>> allGenericDeclarationsWithAnnotations;
    private final Object allGenericDeclarationsWithSuperQualifier = null;
    private final Object allGenericDeclarationsWithMultitypeDeclarations = null;

    public Main(Path rootDir) {
        List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit = allFilesToCompilationUnits(Stream.of(rootDir));
        this.allGenericDeclarationsOnConstructors = findAllGenericConstructors(fileToCompilationUnit);
        this.allGenericDeclarationsWithAnnotations = findGenericDeclarationsWithAnnotations(fileToCompilationUnit);
    }

    public List<Tuple2<Path, List<ConstructorDeclaration>>> getAllGenericDeclarationsOnConstructors() {
        return allGenericDeclarationsOnConstructors;
    }

    public List<Tuple2<Path, List<TypeParameter>>> getAllGenericDeclarationsWithAnnotations() {
        return allGenericDeclarationsWithAnnotations;
    }

    public Object getAllGenericDeclarationsWithSuperQualifier() {
        return allGenericDeclarationsWithSuperQualifier;
    }

    public Object getAllGenericDeclarationsWithMultitypeDeclarations() {
        return allGenericDeclarationsWithMultitypeDeclarations;
    }

    public List<Tuple2<Path, List<ConstructorDeclaration>>>
    findAllGenericConstructors(
            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
        return fileToCompilationUnit
                .stream()
                .map(tuple -> tuple.map2(this::findConstructorsWithParameters))
                .filter(pathListTuple2 -> !pathListTuple2._2().isEmpty())
                .collect(toList());
    }

    private List<ConstructorDeclaration> findConstructorsWithParameters(CompilationUnit compilationUnit) {
        return compilationUnit
                .getTypes()
                .stream()
                .filter(type -> type instanceof ClassOrInterfaceDeclaration)
                .map(type -> (ClassOrInterfaceDeclaration) type)
                .map(ClassOrInterfaceDeclaration::getConstructors)
                .flatMap(Collection::stream)
                .filter(constructorDeclaration
                        -> constructorDeclaration.getTypeParameters().isNonEmpty())
                .collect(toList());
    }

    public List<Tuple2<Path, List<NodeWithTypeParameters>>>
    findGenericDeclarationsWithAnnotations(
            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {

        return fileToCompilationUnit
                .stream()
                .map(tuple -> tuple.map2(compilationUnit ->
                    compilationUnit
                            .getTypes()
                            .stream()
                            .filter(typeDeclaration -> typeDeclaration instanceof ClassOrInterfaceDeclaration)
                            .map(typeDeclaration -> (ClassOrInterfaceDeclaration) typeDeclaration)
                            .flatMap(classOrInterfaceDeclaration -> Streams.concat(
                                    Stream.of(classOrInterfaceDeclaration),
                                    classOrInterfaceDeclaration.getMethods().stream().map(CallableDeclaration::getTypeParameters).flatMap(Collection::stream),
                                    classOrInterfaceDeclaration.getConstructors().stream().map(CallableDeclaration::getTypeParameters).flatMap(Collection::stream)))
                            .collect(toList())))
                .collect(toList());
    }


    private List<Tuple2<Path, CompilationUnit>> allFilesToCompilationUnits(Stream<Path> paths) {
        return paths
                .flatMap(directory -> findAllFiles(directory, ".java").stream())
                .map(this::fileToCompilationUnit)
                .filter(result -> result._2().isRight())
                .map(rightResults -> rightResults.map2(Either::get))
                .collect(toList());
    }

    private Tuple2<Path, Either<Exception, CompilationUnit>> fileToCompilationUnit(Path file) {
        try {
            return new Tuple2<>(file, Either.right(JavaParser.parse(file)));
        } catch (IOException e) {
            return new Tuple2<>(file, Either.left(e));
        }
    }

    private List<Path> findAllFiles(Path directory, String fileType) {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directory)) {
            for (Path path : paths) {
                if (Files.isDirectory(path)) {
                    files.addAll(findAllFiles(path, fileType));
                }
                else if (path.toString().endsWith(fileType)) {
                    files.add(path);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
