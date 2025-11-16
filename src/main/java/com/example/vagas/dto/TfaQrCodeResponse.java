package com.example.vagas.dto;

import lombok.Data;

@Data
public class TfaQrCodeResponse {
    /**
     * URL no formato otpauth:// que o frontend usará para gerar a imagem do QR Code.
     */
    private String qrCodeUrl;
}