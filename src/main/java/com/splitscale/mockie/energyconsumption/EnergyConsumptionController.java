package com.splitscale.mockie.energyconsumption;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.splitscale.Loglemon;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/energy/consumption")
public class EnergyConsumptionController {

  @ResponseBody
  @GetMapping
  public ResponseEntity<List<EnergyConsumption>> readEnergyConsumption() {
    Loglemon.sendLog("{\"msg\": \"readEnergyConsumption() called.\"}");
    List<EnergyConsumption> allEnergyConsumptions = EnergyConsumptionDatabase.getAllEnergyConsumptions();
    Loglemon.sendLog("{\"msg\": \"Returning all energy consumptions.\"}");
    return new ResponseEntity<List<EnergyConsumption>>(allEnergyConsumptions, HttpStatus.OK);
  }

  @ResponseBody
  @PostMapping
  public ResponseEntity<EnergyConsumption> addEnergyConsumption(@RequestBody Map<String, Object> energyConsumptionMap) {
    Loglemon.sendLog("{\"msg\": \"addEnergyConsumption() called.\"}");
    String id = (String) energyConsumptionMap.get("id");
    String energyConsumption = (String) energyConsumptionMap.get("energyConsumption");
    String description = (String) energyConsumptionMap.get("description");
    String importance = (String) energyConsumptionMap.get("importance");

    EnergyConsumption newEnergyConsumption = new EnergyConsumption(id, energyConsumption, description, importance);
    EnergyConsumptionDatabase.addEnergyConsumption(newEnergyConsumption);

    Loglemon.sendLog("{\"msg\": \"New energy consumption added with ID: " + id + "\"}");
    return new ResponseEntity<>(newEnergyConsumption, HttpStatus.CREATED);
  }

  @ResponseBody
  @PutMapping("/{id}")
  public ResponseEntity<EnergyConsumption> editEnergyConsumption(
      @PathVariable("id") String id, @RequestBody EnergyConsumption energyConsumption) {
    EnergyConsumption oldEnergyConsumption = EnergyConsumptionDatabase.getEnergyConsumption(id);
    if (oldEnergyConsumption == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    oldEnergyConsumption.setEnergyConsumption(energyConsumption.getEnergyConsumption());
    oldEnergyConsumption.setDescription(energyConsumption.getDescription());
    oldEnergyConsumption.setImportance(energyConsumption.getImportance());
    EnergyConsumptionDatabase.editEnergyConsumption(id, oldEnergyConsumption);
    return new ResponseEntity<>(oldEnergyConsumption, HttpStatus.OK);
  }

  @ResponseBody
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteEnergyConsumption(@PathVariable("id") String id) {
    boolean isDeleted = EnergyConsumptionDatabase.deleteEnergyConsumption(id);
    if (!isDeleted) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ResponseBody
  @GetMapping("/{id}")
  public ResponseEntity<EnergyConsumption> readEnergyConsumption(@PathVariable("id") String id) {
    EnergyConsumption energyConsumption = EnergyConsumptionDatabase.getEnergyConsumption(id);
    if (energyConsumption == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(energyConsumption, HttpStatus.OK);
  }

  @ResponseBody
  @GetMapping("/filter/{importance}")
  public ResponseEntity<List<EnergyConsumption>> filterEnergyConsumptions(
      @PathVariable("importance") String importance) {
    List<EnergyConsumption> filteredEnergyConsumptions = EnergyConsumptionDatabase
        .filterEnergyConsumptionsByImportance(importance);
    return new ResponseEntity<>(filteredEnergyConsumptions, HttpStatus.OK);
  }

  @ResponseBody
  @GetMapping("/search/{query}")
  public ResponseEntity<List<EnergyConsumption>> searchEnergyConsumptions(@PathVariable("query") String query) {
    List<EnergyConsumption> matchingEnergyConsumptions = EnergyConsumptionDatabase.searchEnergyConsumptions(query);
    return new ResponseEntity<>(matchingEnergyConsumptions, HttpStatus.OK);
  }
}
