package fr.adrienbrault.idea.symfony2plugin.tests.config.yaml;

import com.jetbrains.php.lang.PhpFileType;
import fr.adrienbrault.idea.symfony2plugin.tests.SymfonyLightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 *
 * @see fr.adrienbrault.idea.symfony2plugin.config.yaml.YamlCompletionContributor
 */
public class YamlCompletionContributorTest extends SymfonyLightCodeInsightFixtureTestCase {

    public void setUp() throws Exception {
        super.setUp();

        myFixture.copyFileToProject("services.xml");
        myFixture.configureByText("config_foo.yml", "");
        myFixture.configureByFile("YamlCompletionContributor.php");
        myFixture.configureByFile("tagged.services.xml");
    }

    public String getTestDataPath() {
        return new File(this.getClass().getResource("fixtures").getFile()).getAbsolutePath();
    }

    public void testResourcesInsideSameDirectoryProvidesCompletion() {
        assertCompletionContains("config.yml", "imports:\n" +
                "    - { resource: <caret> }",
            "config_foo.yml"
        );
    }

    /**
     * @see fr.adrienbrault.idea.symfony2plugin.config.yaml.YamlCompletionContributor.RepositoryClassCompletionProvider
     */
    public void testRepositoryClassCompletionProvider() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php\n" +
                "\n" +
                "namespace Doctrine\\Common\\Persistence {\n" +
                "    interface ObjectRepository {};\n" +
                "}\n" +
                "\n" +
                "namespace Foo\\Bar {\n" +
                "    use Doctrine\\Common\\Persistence\\ObjectRepository;\n" +
                "    abstract class BarRepository implements ObjectRepository { }\n" +
                "}"
        );

        for (String s : new String[]{"orm.yml", "couchdb.yml", "odm.yml", "mongodb.yml"}) {
            assertCompletionContains(
                "foo." + s,
                "Foo:\n  repositoryClass: <caret>",
                "Foo\\Bar\\BarRepository"
            );

            assertCompletionResultEquals(
                "foo." + s,
                "Foo:\n  repositoryClass: Foo\\Bar\\<caret>",
                "Foo:\n  repositoryClass: Foo\\Bar\\BarRepository"
            );
        }
    }

    public void testRouteRequirementsCompletion() {
        assertCompletionContains("routing.yml", "" +
                "foo:\n" +
                "    pattern:  /hello/{name}\n" +
                "    requirements:\n" +
                "      'n<caret>'\n",
            "name"
        );

        assertCompletionContains("routing.yml", "" +
                "foo:\n" +
                "    pattern:  /hello/{name}\n" +
                "    requirements:\n" +
                "      n<caret>\n",
            "name"
        );
    }

    public void testRouteControllerActionCompletion() {
        assertCompletionContains("routing.yml", "" +
                "foo:\n" +
                "    pattern:  /hello/{name}\n" +
                "    defaults: { _controller: <caret> }",
            "FooBundle:Foo:foo"
        );
    }

    public void testClassCompletion() {
        assertCompletionContains("routing.yml", "" +
            "services:\n" +
            "    foo:\n" +
            "       class: <caret>\n",
            "FooController"
        );

        assertCompletionContains("routing.yml", "" +
            "services:\n" +
            "    foo:\n" +
            "       class: \"<caret>\"\n",
            "FooController"
        );

        assertCompletionContains("routing.yml", "" +
            "services:\n" +
            "    foo:\n" +
            "       class: '<caret>'\n",
            "FooController"
        );

        assertCompletionContains("routing.yml", "" +
            "services:\n" +
            "    foo:\n" +
            "       factory_class: <caret>\n",
            "FooController"
        );

        assertCompletionContains("routing.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       autowiring_types: <caret>\n",
            "FooController"
        );

        assertCompletionNotContains("routing.yml", "" +
                "servicesa:\n" +
                "    foo:\n" +
                "       factory_class: <caret>\n",
            "FooController"
        );
    }

    public void testParentAndServiceClassCompletion() {
        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       parent: <caret>\n",
            "foo"
        );

        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       factory_service: <caret>\n",
            "foo"
        );
    }

    public void testTagsProvideCompletion() {
        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       tags: { name: <caret>}\n",
            "my_nice_tag"
        );

        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       tags: [ <caret> ]\n",
            "my_nice_tag"
        );

        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo:\n" +
                "       tags: \n" +
                "           - <caret>\n",
            "my_nice_tag"
        );
    }

    public void testParameterCompletion() {
        assertCompletionContains("services.yml", "" +
                "services:\n" +
                "    foo: [%<caret>]",
            "foo_parameter"
        );
    }

    public void testParameterCompletionInsertHandler() {
        assertCompletionResultEquals("services.yml", "services:\n" +
                "    foo: [%foo_paramet<caret>%]",
            "services:\n" +
                "    foo: [%foo_parameter%]"
        );

        assertCompletionResultEquals("services.yml", "services:\n" +
                "    foo: ['%foo_paramet<caret>']",
            "services:\n" +
                "    foo: ['%foo_parameter%']"
        );

        assertCompletionResultEquals("services.yml", "services:\n" +
                "    foo: [\"%foo_paramet<caret>\"]",
            "services:\n" +
                "    foo: [\"%foo_parameter%\"]"
        );
    }
}
