package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.model.PatchPublicHolidayModel;
import com.absencia.diginamic.service.PublicHolidayService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public-holidays")
public class PublicHolidayController {
	private final PublicHolidayService publicHolidayService;

	public PublicHolidayController(final PublicHolidayService publicHolidayService) {
		this.publicHolidayService = publicHolidayService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<List<PublicHoliday>> getPublicHolidays(@PathVariable final int year) {
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);

		return ResponseEntity.ok(publicHolidays);
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@Secured("ADMINISTRATOR")
	public ResponseEntity<Map<String, String>> patchPublicHoliday(@PathVariable final long id, @ModelAttribute @Valid final PatchPublicHolidayModel model) {
		final PublicHoliday publicHoliday = publicHolidayService.findOneById(id);

		// Verify that the public holiday exists
		if (publicHoliday == null) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("message", "Ce jour férié n'existe pas."));
		}

		// Verify that the public holiday date is not passed
		if (publicHoliday.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("message", "Ce jour férié est déjà passé."));
		}

		publicHoliday.setWorked(model.isWorked());

		publicHolidayService.save(publicHoliday);

		return ResponseEntity.ok(Map.of("message", "Le jour férié a été modifié."));
	}

	@PostMapping("/reload")
	@Secured("ADMINISTRATOR")
	public ResponseEntity<Map<String, String>> reloadPublicHoliday() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://calendrier.api.gouv.fr/jours-feries/metropole.json"))
					.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {

				JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

				publicHolidayService.clearTable();

				for (String date : jsonObject.keySet()) {
					String label = jsonObject.get(date).getAsString();
					PublicHoliday publicHoliday = new PublicHoliday();
					publicHoliday.setWorked(false);
					publicHoliday.setDate(LocalDate.parse(date));
					publicHoliday.setLabel(label);
					publicHolidayService.save(publicHoliday);
				}

				return ResponseEntity.ok(Map.of("message", "Les jours fériés ont été mis à jour avec succès."));
			} else {
				return ResponseEntity.badRequest().body(Map.of("message", "Échec de la récupération des jours fériés depuis l'API."));
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("message", "Une erreur s'est produite lors de la récupération des jours fériés depuis l'API."));
		}
	}
}