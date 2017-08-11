package com.implementsblog.gencon;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import io.vavr.Tuple2;
import io.vavr.control.Either;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by justinrogers on 7/10/17.
 */
public class Main {

    private final Path rootDir;

    public Main(Path rootDir) {
        this.rootDir = rootDir;
    }

    public Stream<Tuple2<Path, List<ConstructorDeclaration>>> getAllGenericDeclarationsOnConstructors() {
        return findAllGenericConstructors(allFilesToCompilationUnits(rootDir));
    }

    public Stream<Tuple2<Path, List<MethodDeclaration>>> getGenericDeclarationsOnMethods() {
        return findAllGenericMethods(allFilesToCompilationUnits(rootDir));
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

    public Stream<Tuple2<Path, List<ConstructorDeclaration>>>
    findAllGenericConstructors(
            Stream<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
        return fileToCompilationUnit
                .map(tuple -> tuple.map2(this::findConstructorsWithParameters))
                .filter(pathListTuple2 -> !pathListTuple2._2().isEmpty());
    }

    public Stream<Tuple2<Path, List<MethodDeclaration>>>
    findAllGenericMethods(Stream<Tuple2<Path, CompilationUnit>> fileToCompilationUnit) {
        return fileToCompilationUnit
                .map(tuple -> tuple.map2(this::findMethodsWithGenericParameters))
                .filter(pathListTuple2 -> !pathListTuple2._2().isEmpty());
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

    private List<MethodDeclaration> findMethodsWithGenericParameters(CompilationUnit compilationUnit) {
        return compilationUnit
                .getTypes()
                .stream()
                .map(NodeWithMembers::getMethods)
                .flatMap(Collection::stream)
                .filter(methodDeclaration -> methodDeclaration.getTypeParameters().isNonEmpty())
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

    private Stream<Tuple2<Path, CompilationUnit>> allFilesToCompilationUnits(Path path) {
        return findAllFiles(path, ".java")
                .stream()
                .map(this::fileToCompilationUnit)
                .filter(result -> result._2().isRight())
                .map(rightResults -> rightResults.map2(Either::get));
    }

    private Tuple2<Path, Either<Exception, CompilationUnit>> fileToCompilationUnit(Path file) {
        try {
            return new Tuple2<>(file, Either.right(JavaParser.parse(file)));
        } catch (Exception e) {
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

    public Tuple2<Integer, Integer> toCounts(Stream<? extends Tuple2<Path, ? extends List<? extends NodeWithDeclaration>>> stream) {
        return stream
                .peek(pathToDeclarations -> {
                    System.out.println(pathToDeclarations._1());
                    pathToDeclarations._2().forEach(constructorDeclaration -> {
                        Arrays.stream(constructorDeclaration.toString().split("\n"))
                                .filter(line -> !line.matches("\\s*?/?\\*{1,2}/?.*"))
                                .limit(10)
                                .forEach(line -> System.out.println("\t" + line));

                    });
                })
                .map(pathListTuple2 -> new Tuple2<>(1, pathListTuple2._2().size()))
                .reduce(
                        new Tuple2<>(0, 0),
                        (count1, count2)
                                -> new Tuple2<>(
                                count1._1() + count2._1(),
                                count1._2() + count2._2()));
    }


    public static void main(String[] args) {
        Main m = new Main(Paths.get(args[0]));
        Tuple2<Integer, Integer> counts
                = m.toCounts(m.getGenericDeclarationsOnMethods());

        System.out.println("Total Files: " + counts._1());
        System.out.println("Total Generic Constructors: " + counts._2());
    }


}
