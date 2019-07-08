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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
//扫描mapper接口
@MapperScan(basePackages = {"com.gavin.mapper.master"}, sqlSessionTemplateRef  = "masterSqlSessionTemplate")
public class MasterDruidConfiguration {
	// 读取数据源配置信息
	@ConfigurationProperties(prefix = "spring.datasource.druid.master")
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource dataSource() throws SQLException{
        return DruidDataSourceBuilder.create().build();
    }

    
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();  
        // 加载mybatis配置文件
        Resource resourcesMybatis = resolver.getResource("classpath:/mybatis/mybatis-config.xml");  
        // 加载mapper文件
        Resource[] resourcesMapper = resolver.getResources("classpath*:/mybatis/master/*.xml");
        
        bean.setMapperLocations(resourcesMapper);//对应mapper.xml的具体位置
        bean.setConfigLocation(resourcesMybatis);
        return bean.getObject();
    }
 
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
