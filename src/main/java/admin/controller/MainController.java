package admin.controller;


import admin.entity.Supplier;
import admin.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/admin")
public class MainController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Supplier> getAllUsers() {
        return supplierRepository.findAll();
    }
}
