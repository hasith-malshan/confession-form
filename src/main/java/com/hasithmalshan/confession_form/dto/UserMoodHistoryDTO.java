package com.hasithmalshan.confession_form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMoodHistoryDTO {
    private Long userId;
    private List<UserMoodHistoryEntryDTO> entries;
}
