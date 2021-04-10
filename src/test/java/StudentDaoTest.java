import com.yc.AppConfig;
import com.yc.biz.StudentBizImpl;
import com.yc.dao.StudentDao;
import com.yc.dao.StudentDaoJpaImpl;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @program: testSpring
 * @description: wu
 * @author: oyx
 * @create: 2021-04-04 14:18
 */
public class StudentDaoTest extends TestCase {
    private StudentDao studentDao;
    private StudentBizImpl studentBizImpl;
    private ApplicationContext ac;
    public void setUp() throws Exception
    {
        ac= new AnnotationConfigApplicationContext(AppConfig.class);
        //1.能否自动完成 实例化对象-》IOC 控制反转 -》由容器实例化对象  由容器完成
       studentDao=  new StudentDaoJpaImpl();

       studentBizImpl =new StudentBizImpl();
        //studentBizImpl= (StudentBizImpl) ac.getBean("StudentBizImpl");
        //2.能否自动完成 装配过程-》DI 依赖注入 -》由容器装配对象
        studentBizImpl.setStudentDao(studentDao);
    }

    public void testAdd()
    {
        studentDao.add("张三");
    }

    public void testUpdate()
    {
        studentDao.update("张三");
    }

    public void testBizAdd()
    {
        studentBizImpl.add("张三");
    }
}
