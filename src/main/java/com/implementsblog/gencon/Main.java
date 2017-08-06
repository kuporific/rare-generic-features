package com.implementsblog.gencon;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.google.common.collect.Streams;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by justinrogers on 7/10/17.
 */
public class Main {

    private final List<Tuple2<Path, List<ConstructorDeclaration>>> allGenericDeclarationsOnConstructors;
//    private final List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> allGenericDeclarationsWithAnnotations;
//    private final List<?> allGenericUsagesWithAnnotations;
//    private final List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> allGenericDeclarationsWithMultitypeDeclarations;

    public Main(Path rootDir) {
        List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit = allFilesToCompilationUnits(rootDir);
        this.allGenericDeclarationsOnConstructors = findAllGenericConstructors(fileToCompilationUnit);
//        this.allGenericDeclarationsWithAnnotations = findGenericDeclarationsWithAnnotations(fileToCompilationUnit);
//        this.allGenericUsagesWithAnnotations = findGenericUsagesWithAnnotations(fileToCompilationUnit);
//        this.allGenericDeclarationsWithMultitypeDeclarations = findGenericDeclarationsWithMultitypeDeclarations(fileToCompilationUnit);
    }

    public List<Tuple2<Path, List<ConstructorDeclaration>>> getAllGenericDeclarationsOnConstructors() {
        return allGenericDeclarationsOnConstructors;
    }

//    public List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> getAllGenericDeclarationsWithAnnotations() {
//        return allGenericDeclarationsWithAnnotations;
//    }
//
//    public List<?> getAllGenericUsagesWithAnnotations() {
//        return allGenericUsagesWithAnnotations;
//    }
//
//    public List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> getAllGenericDeclarationsWithMultitypeDeclarations() {
//        return allGenericDeclarationsWithMultitypeDeclarations;
//    }

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

//    private List<Tuple2<Path, List<NodeWithTypeParameters<?>>>>
//    findGenericDeclarations(
//            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
//        return fileToCompilationUnit
//                .stream()
//                .map(tuple -> tuple.map2(compilationUnit ->
//                        compilationUnit
//                                .getTypes()
//                                .stream()
//                                .filter(ClassOrInterfaceDeclaration.class::isInstance)
//                                .map(typeDeclaration -> (ClassOrInterfaceDeclaration) typeDeclaration)
//                                .flatMap(classOrInterfaceDeclaration -> Streams.<NodeWithTypeParameters<?>>concat(
//                                        Stream.of(classOrInterfaceDeclaration),
//                                        classOrInterfaceDeclaration.getMethods().stream(),
//                                        classOrInterfaceDeclaration.getConstructors().stream()))
//                                .collect(toList())))
//                .collect(toList());
//    }
//
//    private List<Tuple2<Path, List<NodeWithTypeParameters<?>>>>
//    findGenericDeclarationsWithAnnotations(
//            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
//        return findGenericDeclarations(fileToCompilationUnit)
//                .stream()
//                .map(tuple -> tuple.map2(nodesWithTypeParameters
//                        -> nodesWithTypeParameters
//                        .stream()
//                        .filter(nodeWithTypeParameters
//                                -> nodeWithTypeParameters
//                                .getTypeParameters()
//                                .stream()
//                                .map(TypeParameter::getAnnotations)
//                                .anyMatch(annotationExprs -> annotationExprs != null && !annotationExprs.isEmpty()))
//                        .collect(toList())))
//                .filter(tuple -> !tuple._2().isEmpty())
//                .collect(toList());
//    }
//
//
//    private List<Tuple2<Path, List<NodeWithTypeParameters<?>>>>
//    findGenericUsagesWithAnnotations(
//            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
//        return null;
//    }
//
//    private List<Tuple2<Path, List<NodeWithTypeParameters<?>>>>
//    findGenericDeclarationsWithMultitypeDeclarations(
//            List<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
//        return findGenericDeclarations(fileToCompilationUnit)
//                .stream()
//                .map(tuple -> tuple.map2(nodesWithTypeParameters
//                        -> nodesWithTypeParameters
//                        .stream()
//                        .filter(nodeWithTypeParameters
//                                -> nodeWithTypeParameters
//                                .getTypeParameters()
//                                .stream()
//                                .map(TypeParameter::getTypeBound)
//                                .anyMatch(typeBound -> typeBound!= null && typeBound.size() > 1))
//                        .collect(toList())))
//                .filter(tuple -> !tuple._2().isEmpty())
//                .collect(toList());
//    }

    private List<Tuple2<Path, CompilationUnit>> allFilesToCompilationUnits(Path path) {
        return findAllFiles(path, ".java")
                .stream()
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

    private List<Path> findAllFiles(Path path, String fileType) {
        if (Files.isDirectory(path)) {
            List<Path> files = new ArrayList<>();
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
                for (Path aPath : paths) {
                    files.addAll(findAllFiles(aPath, fileType));
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return files;
        }
        else if (Files.isRegularFile(path)
                && path.toString().endsWith(fileType)) {
            return Collections.singletonList(path);
        }
        else {
            return Collections.emptyList();
        }
    }
}
