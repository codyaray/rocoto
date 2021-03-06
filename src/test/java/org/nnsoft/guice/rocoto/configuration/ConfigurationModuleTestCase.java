/*
 *    Copyright 2009-2011 The 99 Software Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.nnsoft.guice.rocoto.configuration;

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;

import org.testng.annotations.Test;

/**
 *
 */
public final class ConfigurationModuleTestCase
{

    @Inject
    private IBatisConfiguration iBatisConfiguration;

    @Inject
    private JDBCConfiguration jdbcConfiguration;

    @Inject
    private LdapConfiguration ldapConfiguration;

    @Inject
    private MemcachedConfiguration memcachedConfiguration;

    @Inject
    private ProxyConfiguration proxyConfiguration;

    public void setiBatisConfiguration( IBatisConfiguration iBatisConfiguration )
    {
        this.iBatisConfiguration = iBatisConfiguration;
    }

    public void setJdbcConfiguration( JDBCConfiguration jdbcConfiguration )
    {
        this.jdbcConfiguration = jdbcConfiguration;
    }

    public void setLdapConfiguration( LdapConfiguration ldapConfiguration )
    {
        this.ldapConfiguration = ldapConfiguration;
    }

    public void setMemcachedConfiguration( MemcachedConfiguration memcachedConfiguration )
    {
        this.memcachedConfiguration = memcachedConfiguration;
    }

    public void setProxyConfiguration( ProxyConfiguration proxyConfiguration )
    {
        this.proxyConfiguration = proxyConfiguration;
    }

    @Test
    public void doInject()
    {
        createInjector( new ConfigurationModule()
        {

            @Override
            protected void bindConfigurations()
            {
                bindEnvironmentVariables();
                bindSystemProperties();

                bindProperties( URI.create( "classpath:/org/nnsoft/guice/rocoto/configuration/ldap.properties" ) );
                bindProperties( "proxy.xml" ).inXMLFormat();

                File parentConf = new File( "src/test/data/org/nnsoft" );
                bindProperties( new File( parentConf, "ibatis.properties" ) );
                bindProperties( new File( parentConf, "guice/jdbc.properties" ) );
                bindProperties( new File( parentConf, "guice/rocoto/configuration/memcached.xml" ) ).inXMLFormat();
            }

        } ).injectMembers( this );
    }

    @Test( dependsOnMethods = "doInject" )
    public void verifyIBatisConfiguration()
    {
        assert "test".equals( this.iBatisConfiguration.getEnvironmentId() );
        assert this.iBatisConfiguration.isLazyLoadingEnabled();
    }

    @Test( dependsOnMethods = "doInject" )
    public void verifyJDBCConfiguration()
    {
        assert "com.mysql.jdbc.Driver".equals( this.jdbcConfiguration.getDriver() );
        assert "jdbc:mysql://localhost:3306/rocoto".equals( this.jdbcConfiguration.getUrl() );
        assert "simone".equals( this.jdbcConfiguration.getUsername() );
        assert "rocoto2010".equals( this.jdbcConfiguration.getPassword() );
        assert this.jdbcConfiguration.isAutoCommit();
    }

    @Test( dependsOnMethods = "doInject" )
    public void verifyLdapConfiguration()
    {
        assert "ldap.${not.found}".equals( this.ldapConfiguration.getHost() );
        assert 389 == this.ldapConfiguration.getPort();
        assert this.ldapConfiguration.getBaseDN().indexOf( '$' ) < 0;
        assert "".equals( this.ldapConfiguration.getUser() );
    }

    @Test( dependsOnMethods = "doInject" )
    public void verifyMemcachedConfiguration()
    {
        assert "test_".equals( this.memcachedConfiguration.getKeyPrefix() );
        assert this.memcachedConfiguration.isCompressionEnabled();
    }

    @Test( dependsOnMethods = "doInject" )
    public void verifyProxyConfiguration()
    {
        assert "localhost".equals( this.proxyConfiguration.getHost() );
        assert 8180 == this.proxyConfiguration.getPort();
    }

}
