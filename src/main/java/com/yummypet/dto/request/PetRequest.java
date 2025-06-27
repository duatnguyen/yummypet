package com.yummypet.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.yummypet.enums.HealthStatus;
import com.yummypet.enums.VaccinationStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetRequest {
    @NotBlank(message = "Tên thú cưng không được để trống")
    @Size(max = 100, message = "Tên thú cưng phải có độ dài từ 2 đến 100 ký tự")
    private String name;

    @NotNull(message = "Danh mục thú cưng không được để trống")
    @Min(value = 1, message = "Danh mục thú cưng phải lớn hơn 0")
    private Integer categoryId;

    @NotBlank(message = "Giới tính không được để trống")
    @Size(max = 10, message = "Giới tính phải có độ dài từ 2 đến 10 ký tự")
    private String gender;

    @NotBlank(message = "Tuổi không được để trống")
    @Min(value = 0, message = "Tuổi phải lớn hơn 0")
    @Max(value = 100, message = "Tuổi phải nhỏ hơn 100")
    private Integer ageMonth;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 500, message = "Mô tả phải có độ dài từ 2 đến 500 ký tự")
    private String description;

    @NotBlank(message = "Loài thú cưng không được để trống")
    @Size(max = 100, message = "Loài thú cưng phải có độ dài từ 2 đến 100 ký tự")
    private String species;

    @NotBlank(message = "Giống thú cưng không được để trống")
    @Size(max = 100, message = "Giống thú cưng phải có độ dài từ 2 đến 100 ký tự")
    private String breed;

    @NotBlank(message = "Màu sắc không được để trống")
    @Size(max = 100, message = "Màu sắc phải có độ dài từ 2 đến 100 ký tự")
    private String color;

    @NotBlank(message = "Cân nặng không được để trống")
    @Size(max = 100, message = "Cân nặng phải có độ dài từ 2 đến 100 ký tự")
    private String weight;

    @NotBlank(message = "Tình trạng sức khỏe không được để trống")
    @Size(max = 100, message = "Tình trạng sức khỏe phải có độ dài từ 2 đến 100 ký tự")
    private HealthStatus healthStatus = HealthStatus.unknown;

    @NotBlank(message = "Thông tin chứng nhận không được để trống")
    @Size(max = 100, message = "Thông tin chứng nhận phải có độ dài từ 2 đến 100 ký tự")
    private String certificateInfo;

    @NotNull(message = "Ngày đến không được để trống")
    private LocalDate arrivalDate = LocalDate.now();

    @NotBlank(message = "Tình trạng tiêm chủng không được để trống")
    @Size(max = 100, message = "Tình trạng tiêm chủng phải có độ dài từ 2 đến 100 ký tự")
    private VaccinationStatus vaccinationStatus = VaccinationStatus.unknown;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private boolean isActive = true;

    @DecimalMin(value = "0.0", message = "Giá nhập phải lớn hơn hoặc bằng 0")
    private BigDecimal costPrice;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @Valid
    private List<PetImageRequest> images = new ArrayList<>();
}
