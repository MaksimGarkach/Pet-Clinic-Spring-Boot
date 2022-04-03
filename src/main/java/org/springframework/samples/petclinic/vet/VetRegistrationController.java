package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.registration.Role;
import org.springframework.samples.petclinic.registration.User;
import org.springframework.samples.petclinic.registration.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class VetRegistrationController {

	private final VetRepository vets;

	private final UserRepo userRepo;

	private final SpecialtyRepository specialtyRepository;

	public VetRegistrationController(VetRepository vets, UserRepo userRepo, SpecialtyRepository specialtyRepository) {
		this.vets = vets;
		this.userRepo = userRepo;
		this.specialtyRepository = specialtyRepository;
	}

	@ModelAttribute("specialty")
	public Collection<Specialty> populateSpecialties() {
		return this.specialtyRepository.findSpecialties();
	}

	@GetMapping("/registrationVet")
	public String registrationVet(ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		User user = new User();
		model.put("user", user);
		return "vets/vetRegistrationForm";
	}

	@PostMapping("/registrationVet")
	public String addVet(User user, Map<String, Object> model, @Valid Vet vet, BindingResult result) {
		User userFromDb = userRepo.findByUsername(user.getUsername());

		if (userFromDb != null | result.hasErrors()) {
			model.put("message", "User exists!");
			return "registration/ownerRegistrationForm";
		} else {
			this.vets.save(vet);
			user.setVetId(vets.findById(vet.getId()));
			user.setActive(true);
			user.setRoles(Collections.singleton(Role.VET));
			userRepo.save(user);
			return "vets/vetAccount";
		}
	}
}
