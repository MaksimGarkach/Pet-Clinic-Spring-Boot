/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
class VetController {

	private final VetRepository vets;

	private final SpecialtyRepository specialtyRepository;

	public VetController(VetRepository clinicService, SpecialtyRepository specialtyRepository) {
		this.vets = clinicService;
		this.specialtyRepository = specialtyRepository;
	}

	@ModelAttribute("specialty")
	public Collection<Specialty> populateSpecialties() {
		return this.specialtyRepository.findSpecialties();
	}

	@GetMapping("/vets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Model model) {
		Vet vet = new Vet();
		model.addAttribute(vet);
		Page<Vet> paginated = findPaginated(page);
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		model.addAttribute("listVets", paginated);
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vets.findAll(pageable);
	}

	@GetMapping("/vets/{vetId}")
	public ModelAndView showVet(@PathVariable("vetId") int vetId) {
		ModelAndView mav = new ModelAndView("vets/vetDetails");
		Vet vet = this.vets.findById(vetId);
		mav.addObject(vet);
		return mav;
	}

	@GetMapping("/vets/new")
	public String initCreationForm(ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return "vets/createOrUpdateVetForm";
	}

	@PostMapping("/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "vets/createOrUpdateVetForm";
		} else {
			this.vets.save(vet);
			return "vets/vetList";
		}
	}

	@GetMapping("/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") int vetId, ModelMap model) {
		Vet vet = this.vets.findById(vetId);
		model.put("vet", vet);
		return "vets/createOrUpdateVetForm";
	}

	@PostMapping("/vets/{vetId}/edit")
	public String processUpdateForm(@Valid Vet vet, @PathVariable("vetId") int vetId, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "vets/createOrUpdateVetForm";
		} else {
			vet.setId(vetId);
			this.vets.save(vet);
			return "redirect:/vets/{vetId}";
		}
	}
}
