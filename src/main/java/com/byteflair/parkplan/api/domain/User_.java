package com.byteflair.parkplan.api.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> topoosId;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> email;
}