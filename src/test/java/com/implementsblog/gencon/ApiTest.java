package com.implementsblog.gencon;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import io.vavr.Tuple2;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by justinrogers on 7/21/17.
 */
public class ApiTest {

    @Test
    public void testGenericDeclarationsOnConstructors() {
        final Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample/Example1.java"));
        List<Tuple2<Path, List<ConstructorDeclaration>>> allGenericDeclarationsOnConstructors = m.getAllGenericDeclarationsOnConstructors();
        assertThat(allGenericDeclarationsOnConstructors, hasSize(1));
        assertThat(allGenericDeclarationsOnConstructors.get(0)._2(), hasSize(4));
    }

    @Test
    public void testGenericDeclarationsWithAnnotations() {
        Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample/Example2.java"));
        List<Tuple2<Path, List<NodeWithTypeParameters<?>>>> allGenericDeclarationsWithAnnotations = m.getAllGenericDeclarationsWithAnnotations();
        assertThat(allGenericDeclarationsWithAnnotations, hasSize(1));
        assertThat(allGenericDeclarationsWithAnnotations.get(0)._2(), hasSize(6));
    }

    public void futureTests() {
        final Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample"));
        m.getAllGenericDeclarationsWithSuperQualifier();
        m.getAllGenericDeclarationsWithMultitypeDeclarations();
    }
}
