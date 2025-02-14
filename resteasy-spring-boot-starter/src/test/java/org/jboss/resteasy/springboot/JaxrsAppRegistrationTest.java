package org.jboss.resteasy.springboot;

import org.jboss.resteasy.springboot.sample.*;
import org.junit.jupiter.api.Disabled;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ext.Provider;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Created by facarvalho on 7/19/16.
 *
 * @author Fabio Carvalho (facarvalho@paypal.com or fabiocarvalho777@gmail.com)
 */
//@PrepareForTest(AutoConfigurationPackages.class)
//@MockPolicy(Slf4jMockPolicy.class)
public class JaxrsAppRegistrationTest {

    private static final String DEFINITION_PROPERTY = "resteasy.jaxrs.app.registration";
    private static final String APP_CLASSES_PROPERTY = "resteasy.jaxrs.app.classes";

    private static Set<Class> allPossibleAppClasses;

    static {
        Set<Class> _allPossibleAppClasses = new HashSet<Class>();

        _allPossibleAppClasses.add(TestApplication1.class);
        _allPossibleAppClasses.add(TestApplication2.class);
        _allPossibleAppClasses.add(TestApplication3.class);
        _allPossibleAppClasses.add(TestApplication4.class);
        _allPossibleAppClasses.add(TestApplication5.class);
        _allPossibleAppClasses.add(Application.class);

        allPossibleAppClasses = Collections.unmodifiableSet(_allPossibleAppClasses);
    }

    @BeforeMethod
    public void beforeTest() {
//        PowerMockito.mockStatic(AutoConfigurationPackages.class);
        List<String> packages = new ArrayList<String>();
        packages.add("org.jboss.resteasy.springboot.sample");
//        PowerMockito.when(AutoConfigurationPackages.get(any(BeanFactory.class))).thenReturn(packages);
    }

    @Test
    @Ignore
    public void nullTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn(null);

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(TestApplication1.class);
        expectedRegisteredAppClasses.add(TestApplication2.class);
        expectedRegisteredAppClasses.add(TestApplication4.class);
        expectedRegisteredAppClasses.add(TestApplication5.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    @Ignore
    @Test
    public void autoTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("auto");

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(TestApplication1.class);
        expectedRegisteredAppClasses.add(TestApplication2.class);
        expectedRegisteredAppClasses.add(TestApplication4.class);
        expectedRegisteredAppClasses.add(TestApplication5.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    @Test
    public void beansTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("beans");

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(TestApplication1.class);
        expectedRegisteredAppClasses.add(TestApplication4.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    @Test
    public void propertyTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("property");
        when(configurableEnvironmentMock.getProperty(APP_CLASSES_PROPERTY)).thenReturn("org.jboss.resteasy.springboot.sample.TestApplication3, org.jboss.resteasy.springboot.sample.TestApplication4,org.jboss.resteasy.springboot.sample.TestApplication2");

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(TestApplication2.class);
        expectedRegisteredAppClasses.add(TestApplication4.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    @Test
    @Ignore
    public void scanningTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("scanning");

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(TestApplication1.class);
        expectedRegisteredAppClasses.add(TestApplication2.class);
        expectedRegisteredAppClasses.add(TestApplication4.class);
        expectedRegisteredAppClasses.add(TestApplication5.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Property " + DEFINITION_PROPERTY +
                    " has not been properly set, value blah is invalid. JAX-RS Application classes registration is being set to AUTO.")
    public void invalidRegistrationTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("blah");

        ConfigurableListableBeanFactory beanFactory = mock(ConfigurableListableBeanFactory.class);
        when(beanFactory.getBean(ConfigurableEnvironment.class)).thenReturn(configurableEnvironmentMock);

        ResteasyEmbeddedServletInitializer resteasyEmbeddedServletInitializer = new ResteasyEmbeddedServletInitializer();
        resteasyEmbeddedServletInitializer.postProcessBeanFactory(beanFactory);
    }

    @Test(expectedExceptions = BeansException.class)
    public void classNotFoundTest() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("property");
        when(configurableEnvironmentMock.getProperty(APP_CLASSES_PROPERTY)).thenReturn("org.jboss.resteasy.springboot.sample.TestApplication3, org.jboss.resteasy.springboot.sample.TestApplication4,org.jboss.resteasy.springboot.sample.TestApplication9");

        ConfigurableListableBeanFactory beanFactory = mock(ConfigurableListableBeanFactory.class);
        when(beanFactory.getBean(ConfigurableEnvironment.class)).thenReturn(configurableEnvironmentMock);

        ResteasyEmbeddedServletInitializer resteasyEmbeddedServletInitializer = new ResteasyEmbeddedServletInitializer();
        resteasyEmbeddedServletInitializer.postProcessBeanFactory(beanFactory);
    }

    @Test
    public void testPropertyNoApps() {
        ConfigurableEnvironment configurableEnvironmentMock = mock(ConfigurableEnvironment.class);
        when(configurableEnvironmentMock.getProperty(DEFINITION_PROPERTY)).thenReturn("property");

        Set<Class> expectedRegisteredAppClasses = new HashSet<Class>();
        expectedRegisteredAppClasses.add(Application.class);

        test(configurableEnvironmentMock, expectedRegisteredAppClasses);
    }

    private void test(ConfigurableEnvironment envMock, Set<Class> expectedRegisteredAppClasses) {
        ConfigurableListableBeanFactory beanFactory = prepareTest(envMock);
        performTest(envMock, beanFactory, expectedRegisteredAppClasses);
    }

    private ConfigurableListableBeanFactory prepareTest(ConfigurableEnvironment envMock) {
        ConfigurableListableBeanFactory beanFactory = mock(
                ConfigurableListableBeanFactory.class,
                withSettings().extraInterfaces(BeanDefinitionRegistry.class)
        );

        when(beanFactory.getBean(ConfigurableEnvironment.class)).thenReturn(envMock);
        when(beanFactory.getBeanNamesForAnnotation(Path.class)).thenReturn(new String[]{"testResource1", "testResource2"});
        when(beanFactory.getType("testResource1")).thenReturn((Class) TestResource1.class);
        when(beanFactory.getType("testResource2")).thenReturn((Class) TestResource2.class);

        String definition = envMock.getProperty(DEFINITION_PROPERTY);

        if ((definition != null && definition.equals("beans"))) {
            // Although TestApplication1 and TestApplication4 are not really Spring beans, here we are simulating
            // they are to see how the JAX-RS Application registration behaves
            Map<String, Application> applicationsMap = new HashMap<String, Application>();
            applicationsMap.put("testApplication1", new TestApplication1());
            applicationsMap.put("testApplication4", new TestApplication4());
            when(beanFactory.getBeansOfType(Application.class, true, false)).thenReturn(applicationsMap);
        }

        return beanFactory;
    }

    private void performTest(ConfigurableEnvironment envMock, ConfigurableListableBeanFactory beanFactory, Set<Class> expectedRegisteredAppClasses) {
        String definition = envMock.getProperty(DEFINITION_PROPERTY);
        boolean findSpringBeans = (definition == null || definition.equals("auto") || definition.equals("beans"));
        boolean getAppsProperty = (definition == null || definition.equals("auto") || definition.equals("property"));

        ResteasyEmbeddedServletInitializer resteasyEmbeddedServletInitializer = new ResteasyEmbeddedServletInitializer();
        resteasyEmbeddedServletInitializer.postProcessBeanFactory(beanFactory);

        if (getAppsProperty) {
            verify(beanFactory, VerificationModeFactory.atLeast(3)).getBean(ConfigurableEnvironment.class);
        } else {
            verify(beanFactory, VerificationModeFactory.times(2)).getBean(ConfigurableEnvironment.class);
        }

        verify(beanFactory, VerificationModeFactory.times(findSpringBeans ? 1 : 0)).getBeansOfType(Application.class, true, false);
        verify(beanFactory, VerificationModeFactory.times(1)).getBeanNamesForAnnotation(Path.class);
        verify(beanFactory, VerificationModeFactory.times(1)).getBeanNamesForAnnotation(Provider.class);
        verify(beanFactory, VerificationModeFactory.times(2)).getType(anyString());

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

        Set<Class> expectedNotRegisteredAppClassess = new HashSet<Class>(allPossibleAppClasses);
        for (Class applicationClass : expectedRegisteredAppClasses) {
            verify(registry, VerificationModeFactory.times(1)).registerBeanDefinition(eq(applicationClass.getName()), any(GenericBeanDefinition.class));
            expectedNotRegisteredAppClassess.remove(applicationClass);
        }
        for (Class applicationClass : expectedNotRegisteredAppClassess) {
            verify(registry, VerificationModeFactory.times(0)).registerBeanDefinition(eq(applicationClass.getName()), any(GenericBeanDefinition.class));
        }
    }

}
