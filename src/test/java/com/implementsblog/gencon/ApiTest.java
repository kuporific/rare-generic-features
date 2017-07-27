package com.implementsblog.gencon;

import com.github.javaparser.ast.type.TypeParameter;
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

    private final Main m = new Main(Paths.get("src/test/java/com/implementsblog/gencon/sample"));

    @Test
    public void testGenericDeclarationsOnConstructors() {
        assertThat(m.getAllGenericDeclarationsOnConstructors(), hasSize(4));
    }

    @Test
    public void testGenericDeclarationsWithAnnotations() {
        List<Tuple2<Path, List<TypeParameter>>> allGenericDeclarationsWithAnnotations = m.getAllGenericDeclarationsWithAnnotations();
        allGenericDeclarationsWithAnnotations.stream().forEach(tuple -> {
            System.out.println(tuple._1());
            tuple._2().stream().forEach(typeParameter -> {
                System.out.println("\t" + typeParameter.asString());
            });
        });
        assertThat(allGenericDeclarationsWithAnnotations, hasSize(6));
    }

    public void futureTests() {
        m.getAllGenericDeclarationsWithSuperQualifier();
        m.getAllGenericDeclarationsWithMultitypeDeclarations();
    }
}
