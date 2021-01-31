package com.itheima.mm.service;

import com.itheima.mm.dao.CatlogDao;
import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.TagDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/30$ 22:34$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
public class CourseService {
    public void add(Course course) throws Exception {
//        调用dao层的方法添加学科信息
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        courseDao.add(course);
        SqlSessionFactoryUtils.commitAndClose(sqlSession);

    }

    public PageResult findByPage(QueryPageBean queryPageBean) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        //调用dao层的方法  查询学科总条数
        Long total = courseDao.findTotalCount(queryPageBean);
        //调用dao层的方法  查询当前页的学科列表
        List<Course> courseList = courseDao.findPageList(queryPageBean);
        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return new PageResult(total,courseList);
    }

    public void update(Course course) throws Exception {
        //调用dao层的方法修改学科信息
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        courseDao.update(course);
        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public void deleteById(Integer id) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        //判断当前要删除的学科是否有关联的二级目录
        CatlogDao catlogDao = sqlSession.getMapper(CatlogDao.class);
        //根据courseId到t_catalog表中查询数据条数
        Long catalogCount = catlogDao.findCountByCourseId(id);
        if (catalogCount>0) {
            //有关联的二级目录，不能删除
            throw new RuntimeException("有关联的二级目录，不能删除");
        }
        //判断当前要删除的学科是否有关联的标签
        TagDao tagDao = sqlSession.getMapper(TagDao.class);
        Long tagCount = tagDao.findCountByCourseId(id);
        if (tagCount>0) {
            //有关联的标签，不能删除
            throw new RuntimeException("有关联的标签，不能删除");
        }
        QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
        Long questionCount = questionDao.findCountByCourseId(id);
        if (questionCount>0) {
            //有关联的题目，不能删除
            throw new RuntimeException("有关联的题目，不能删除");
        }
        //如果上述三个关联都没有，则调用dao层的方法进行根据id删除
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        courseDao.deleteById(id);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public List<Course> findAll(String status) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        List<Course> courseList = courseDao.findAll(status);
        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return courseList;
    }
}
