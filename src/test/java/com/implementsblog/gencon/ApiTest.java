package com.implementsblog.gencon;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import io.vavr.Tuple2;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by justinrogers on 7/21/17.
 */
public class ApiTest {

    @Test
    public void testGenericDeclarationsOnConstructors() {
        final Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample/example1"));
        List<Tuple2<Path, List<ConstructorDeclaration>>> allGenericDeclarationsOnConstructors = m.getAllGenericDeclarationsOnConstructors();
        assertThat(allGenericDeclarationsOnConstructors, hasSize(1));
        assertThat(allGenericDeclarationsOnConstructors.get(0)._2(), hasSize(4));
    }

    @Test
    public void testGenericDeclarationsWithAnnotations() {
        Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample/example2"));
        List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> allGenericDeclarationsWithAnnotations = m.getAllGenericDeclarationsWithAnnotations();
        prettyPrint(allGenericDeclarationsWithAnnotations);
        assertThat(allGenericDeclarationsWithAnnotations, hasSize(3));
        assertThat(allGenericDeclarationsWithAnnotations.get(0)._2(), hasSize(5));
        assertThat(allGenericDeclarationsWithAnnotations.get(1)._2(), hasSize(5));
        assertThat(allGenericDeclarationsWithAnnotations.get(2)._2(), hasSize(4));
    }

    @Test
    public void testGenericDeclarationsWithMultitypeDeclarations() {
        Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample/example4"));
        List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> allGenericDeclarationsWithAnnotations = m.getAllGenericDeclarationsWithMultitypeDeclarations();
        prettyPrint(allGenericDeclarationsWithAnnotations);
        assertThat(allGenericDeclarationsWithAnnotations, hasSize(1));
        assertThat(allGenericDeclarationsWithAnnotations.get(0)._2(), hasSize(2));
    }

    private void prettyPrint(List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> data) {
        System.out.println("start");
        data.forEach(tuple2 -> {
            System.out.println("===========");
            System.out.println(tuple2._1());
            tuple2._2().forEach(listItem -> {
                System.out.println("-------------");
                String match = listItem.toString();
                int endOfLineIndex = match.indexOf("\n");
                System.out.println(
                        match.substring(
                                0,
                                endOfLineIndex != -1
                                        ? endOfLineIndex
                                        : match.length()));
            });
        });
        System.out.println("end");
    }
}
