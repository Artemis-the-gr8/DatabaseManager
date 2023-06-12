package com.artemis.the.gr8.databasemanager;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Full Database Test Suite")
@SelectClasses({PlayerDAOTest.class, StatDAOTest.class, SubStatDAOTest.class, StatCombinationDAOTest.class, StatValueDAOTest.class})
public class TestSuite {
}
