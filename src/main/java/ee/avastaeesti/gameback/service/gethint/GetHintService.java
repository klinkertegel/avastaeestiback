package ee.avastaeesti.gameback.service.gethint;

import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class GetHintService {

    private final LocationRepository locationRepository;

    public GetHintService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public String getLocationHint(int locationId) {
        String clue = locationRepository.findClueByLocationId(locationId);
        return clue != null ? clue : "No hint found for location ID: " + locationId; //change to orelsethrow?
    }
}