import com.yc.biz.AppConfig;
import com.yc.biz.HelloWorld;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-04 15:04
 */
public class HelloWorldTest extends TestCase {
    private ApplicationContext ac;  //spring容器
    @Override
    @Before
    public void setUp()
    {
        ac= new AnnotationConfigApplicationContext(AppConfig.class);
    }
    @Test
    public void testHello()
    {
        HelloWorld hw= (HelloWorld) ac.getBean("helloWorld");
        hw.hello();

        HelloWorld hw2=(HelloWorld)ac.getBean("helloWorld");
        hw2.hello();
    }
}
