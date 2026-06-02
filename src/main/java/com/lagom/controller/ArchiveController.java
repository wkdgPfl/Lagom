package com.lagom.controller;

import com.lagom.dto.response.ArchiveDetailResponse;
import com.lagom.dto.response.ArchiveResponse;
import com.lagom.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archives")
@RequiredArgsConstructor
public class ArchiveController {

    private final ArchiveService archiveService;

    // 완료된 통장 목록 조회
    @GetMapping
    public ResponseEntity<List<ArchiveResponse>> getArchives(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(archiveService.getArchives(userId));
    }

    // 완료된 통장 상세 조회
    @GetMapping("/{accountId}")
    public ResponseEntity<ArchiveDetailResponse> getArchiveDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long accountId) {
        return ResponseEntity.ok(archiveService.getArchiveDetail(userId, accountId));
    }
}