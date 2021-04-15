package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.Component.MailSender;
import uz.pdp.lesson5task1.entity.Months;
import uz.pdp.lesson5task1.entity.SalaryTaken;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.entity.enums.MonthEnum;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.SalaryAmountDto;
import uz.pdp.lesson5task1.repository.MonthRepository;
import uz.pdp.lesson5task1.repository.SalaryRepository;
import uz.pdp.lesson5task1.repository.UsersRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    MonthRepository monthRepository;
    @Autowired
    MailSender mailSender;

    public ApiResponse addSalaryTable(SalaryAmountDto salaryAmountDto) {
        Optional<User> salaryTaker = usersRepository.findByEmail(salaryAmountDto.getEmail());
        if (!salaryTaker.isPresent()) return new ApiResponse(false, "Xodim topilmadi");

        Optional<Months> optionalMonths = monthRepository.findById(salaryAmountDto.getMonthsId());
        if (!optionalMonths.isPresent()) return new ApiResponse(false, "Oy noto'g'ri kiritildi");

        SalaryTaken salaryTaken = new SalaryTaken();
        salaryTaken.setAmount(salaryAmountDto.getAmount());
        salaryTaken.setMonths(optionalMonths.get());
        salaryTaken.setOwner(salaryTaker.get());
        salaryRepository.save(salaryTaken);
        return new ApiResponse(true, "Successfully added");
    }

    public ApiResponse giveSalary(String email, Integer monthId) throws MessagingException {
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (!optionalUser.isPresent()) return new ApiResponse(false, "Xodim topilmadi");

        Optional<Months> optionalMonths = monthRepository.findById(monthId);
        if (!optionalUser.isPresent()) return new ApiResponse(false, "Oy id noto'g'ri kiritildi");

        SalaryTaken byOwnerAndMonths = salaryRepository.findByOwnerAndMonths(optionalUser.get(), optionalMonths.get());
        if (byOwnerAndMonths == null) return new ApiResponse(false, "Berilgan parametrlar uchun maosh belgilanmagan");

        byOwnerAndMonths.setPaid(true);
        salaryRepository.save(byOwnerAndMonths);

        boolean mailForConfirmSalary = mailSender.mailForConfirmSalary(email, byOwnerAndMonths.getAmount(), optionalMonths.get().getMonthName().name());
        if (mailForConfirmSalary) return new ApiResponse(true, "Oylik berildi,Email xodimga jo'natildi");
        return new ApiResponse(false, "Xatolik");
    }


    public List<SalaryTaken> getByMonths(Integer monthsId) {
        if (monthsId > 12 || monthsId < 1) {
            return null;
        }
        List<SalaryTaken> byMonths = salaryRepository.findByMonths(monthsId);
        return byMonths;
    }

    public List<SalaryTaken> getByOwnerId(String email) {
        Optional<User> byEmail = usersRepository.findByEmail(email);
        if (!byEmail.isPresent()) return null;
        List<SalaryTaken> byOwners = salaryRepository.findByOwners(byEmail.get().getId());
        return byOwners;

    }

}
