package pl.synowiec.ksb2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {
    private List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1l, "Opel", "Corsa", "Black"));
        carList.add(new Car(2l, "Audi", "A3", "Silver"));
        carList.add(new Car(3l, "BMW", "M4", "White"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable String color) {
        List<Car> colorList = new ArrayList<>();
        boolean add = false;
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getColor().equals(color)) {
                add = colorList.add(carList.get(i));
            }
        }
        if (add) {
            return new ResponseEntity<>(colorList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        boolean add = carList.add(car);
        if (add) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping()
    public ResponseEntity<Car> modCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/modCarByParam")
    public ResponseEntity<Car> modCarByParam(@RequestParam String param,
                                             @RequestParam String value) {
        boolean modify = false;
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getMark().equals(param)) {
                carList.get(i).setMark(value);
                modify = true;
            }
            if (carList.get(i).getModel().equals(param)) {
                carList.get(i).setModel(value);
                modify = true;
            }
            if (carList.get(i).getColor().equals(param)) {
                carList.get(i).setColor(value);
                modify = true;
            }
            if (modify) return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> removeCarById(@PathVariable Long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
