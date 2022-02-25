package org.springframework.samples.petclinic.registration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

	OWNER;

	@Override
	public String getAuthority() {
		return name();
	}

}
