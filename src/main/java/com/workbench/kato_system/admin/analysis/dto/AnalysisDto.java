package com.workbench.kato_system.admin.analysis.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisDto {

  private Integer clientId;

  private String clientName;

	private LocalDate latestOrderDate;

  private Long frequentPurchase;

  private Long totalAmount;

}
