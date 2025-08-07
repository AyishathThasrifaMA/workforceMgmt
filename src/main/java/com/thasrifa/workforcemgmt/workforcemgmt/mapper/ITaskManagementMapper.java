package com.thasrifa.workforcemgmt.workforcemgmt.mapper;


import com.thasrifa.workforcemgmt.workforcemgmt.dto.TaskActivityHistoryDto;
import com.thasrifa.workforcemgmt.workforcemgmt.dto.TaskCommentDto;
import com.thasrifa.workforcemgmt.workforcemgmt.dto.TaskManagementDto;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskActivityHistory;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskComment;
import com.thasrifa.workforcemgmt.workforcemgmt.model.TaskManagement;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


import java.util.List;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {
   ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);


   TaskManagementDto modelToDto(TaskManagement model);


   TaskManagement dtoToModel(TaskManagementDto dto);


   List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);
   TaskCommentDto commentModelToDto(TaskComment comment);
   List<TaskCommentDto> commentModelListToDtoList(List<TaskComment> comments);

   TaskActivityHistoryDto activityModelToDto(TaskActivityHistory activity);
   List<TaskActivityHistoryDto> activityModelListToDtoList(List<TaskActivityHistory> activities);
}
