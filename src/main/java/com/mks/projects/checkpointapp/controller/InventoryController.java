package com.mks.projects.checkpointapp.controller;



import com.mks.projects.checkpointapp.dto.InventoryDto;
import com.mks.projects.checkpointapp.dto.UpdateInventoryRequestDto;
import com.mks.projects.checkpointapp.service.InventoryService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<InventoryDto>> getAllInventoryByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }


    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<Void> updateInventory (@PathVariable Long roomId, @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto){


        inventoryService.updateInventory(roomId, updateInventoryRequestDto);

        return ResponseEntity.noContent().build();
    }



}
