package com.eventhub.api.service;

import com.eventhub.api.dto.RegistrationDTO;
import com.eventhub.api.dto.RegistrationItemDTO;
import com.eventhub.api.exception.ResourceNotFoundException;
import com.eventhub.api.model.*;
import com.eventhub.api.repository.EventRepository;
import com.eventhub.api.repository.RegistrationItemRepository;
import com.eventhub.api.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationItemRepository registrationItemRepository;
    private final UserService userService;
    private final EventRepository eventRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               RegistrationItemRepository registrationItemRepository,
                               UserService userService,
                               EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.registrationItemRepository = registrationItemRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    public List<RegistrationDTO> getAllRegistrations() {
        return registrationRepository.findAll().stream().map(this::toDTO).toList();
    }

    public RegistrationDTO getRegistrationById(Long id) {
        return toDTO(findOrThrow(id));
    }

    public List<RegistrationDTO> getRegistrationsByUser(Long userId) {
        return registrationRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public RegistrationDTO createRegistration(RegistrationDTO dto) {
        User user = userService.findEntityById(dto.getUserId());

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setStatus(Registration.RegistrationStatus.PENDING);

        List<RegistrationItem> items = new ArrayList<>();
        double total = 0.0;

        for (RegistrationItemDTO itemDTO : dto.getItems()) {
            Event event = eventRepository.findById(itemDTO.getEventId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Event not found with id: " + itemDTO.getEventId()));

            RegistrationItem item = new RegistrationItem();
            item.setRegistration(reg);
            item.setEvent(event);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(event.getTicketPrice());
            items.add(item);
            total += event.getTicketPrice() * itemDTO.getQuantity();
        }

        reg.setItems(items);
        reg.setTotalAmount(total);
        return toDTO(registrationRepository.save(reg));
    }

    @Transactional
    public RegistrationDTO updateStatus(Long id, String status) {
        Registration reg = findOrThrow(id);

        try {
            Registration.RegistrationStatus enumStatus =
                    Registration.RegistrationStatus.valueOf(status.toUpperCase());

            reg.setStatus(enumStatus);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }

        return toDTO(registrationRepository.save(reg));
    }
    public void deleteRegistration(Long id) {
        findOrThrow(id);
        registrationRepository.deleteById(id);
    }

    private Registration findOrThrow(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
    }

    private RegistrationDTO toDTO(Registration reg) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setId(reg.getId());
        dto.setUserId(reg.getUser().getId());
        dto.setUserName(reg.getUser().getName());
        dto.setRegistrationDate(reg.getRegistrationDate());
        dto.setStatus(reg.getStatus().name());
        dto.setTotalAmount(reg.getTotalAmount());
        if (reg.getItems() != null) {
            dto.setItems(reg.getItems().stream().map(this::toItemDTO).toList());
        }
        return dto;
    }

    private RegistrationItemDTO toItemDTO(RegistrationItem item) {
        RegistrationItemDTO dto = new RegistrationItemDTO();
        dto.setId(item.getId());
        dto.setEventId(item.getEvent().getId());
        dto.setEventTitle(item.getEvent().getTitle());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
