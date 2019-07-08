package com.gavin.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
// 扫描mapper接口
@MapperScan(basePackages = {"com.gavin.mapper.slave"}, sqlSessionTemplateRef  = "slaveSqlSessionTemplate")
public class SlaveDruidConfiguration {
	// 读取数据源配置信息
	@ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    @Bean(name = "slaveDataSource")
    public DataSource dataSource() throws SQLException{
        return DruidDataSourceBuilder.create().build();
    }

    
    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();  
        // 加载mybatis配置文件
        Resource resourcesMybatis = resolver.getResource("classpath:/mybatis/mybatis-config.xml");
        // 加载mapper文件
        Resource[] resourcesMapper = resolver.getResources("classpath:/mybatis/slave/*.xml");
     
        bean.setMapperLocations(resourcesMapper);
        bean.setConfigLocation(resourcesMybatis);
        return bean.getObject();
    }
 
    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("slaveDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
