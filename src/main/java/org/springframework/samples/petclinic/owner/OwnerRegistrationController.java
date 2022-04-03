package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.registration.Role;
import org.springframework.samples.petclinic.registration.User;
import org.springframework.samples.petclinic.registration.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class OwnerRegistrationController {

	private final OwnerRepository owners;

	private final UserRepo userRepo;

	public OwnerRegistrationController(OwnerRepository owners, UserRepo userRepo) {
		this.owners = owners;
		this.userRepo = userRepo;
	}

	@GetMapping("/registrationOwner")
	public String registrationOwner(Map<String, Object> model) {

		Owner owner = new Owner();
		model.put("owner", owner);

		User user = new User();
		model.put("user", user);
		return "registration/ownerRegistrationForm";
	}

	@PostMapping("/registrationOwner")
	public String addUser(User user, Map<String, Object> model, @Valid Owner owner, BindingResult result) {
		User userFromDb = userRepo.findByUsername(user.getUsername());

		if (userFromDb != null | result.hasErrors()) {
			model.put("message", "User exists!");
			return "registration/ownerRegistrationForm";
		} else {
			this.owners.save(owner);
			user.setOwnerId(owners.findById(owner.getId()));
			user.setActive(true);
			user.setRoles(Collections.singleton(Role.OWNER));
			userRepo.save(user);
			return "/welcome";
		}
	}
}
