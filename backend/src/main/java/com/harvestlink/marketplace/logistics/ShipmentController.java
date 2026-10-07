package com.harvestlink.marketplace.logistics;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/shipments")
    public List<ShipmentResponse> getShipments() {
        return shipmentRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ShipmentResponse::from)
                .toList();
    }

    @GetMapping("/shipments/{id}")
    public ShipmentResponse getShipment(@PathVariable Long id) {
        return shipmentRepository.findById(id)
                .map(ShipmentResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));
    }

    @PatchMapping("/shipments/{id}/status")
    public ShipmentResponse updateShipmentStatus(@PathVariable Long id, @RequestBody ShipmentStatusUpdateRequest request) {
        if (request == null || !StringUtils.hasText(request.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shipment status is required");
        }

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

        try {
            shipment.setStatus(ShipmentStatus.valueOf(request.status().trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported shipment status: " + request.status());
        }

        return ShipmentResponse.from(shipmentRepository.save(shipment));
    }

    public record ShipmentStatusUpdateRequest(String status) {
    }

    public record ShipmentResponse(Long id,
                                  Long orderId,
                                  String carrier,
                                  String trackingCode,
                                  String origin,
                                  String destination,
                                  String status,
                                  String eta,
                                  String notes,
                                  Instant createdAt) {
        public static ShipmentResponse from(Shipment shipment) {
            return new ShipmentResponse(
                    shipment.getId(),
                    shipment.getOrderId(),
                    shipment.getCarrier(),
                    shipment.getTrackingCode(),
                    shipment.getOrigin(),
                    shipment.getDestination(),
                    shipment.getStatus().name(),
                    shipment.getEta(),
                    shipment.getNotes(),
                    shipment.getCreatedAt());
        }
    }
}
