package andersen.parking.service.impl;

import andersen.parking.service.PriceService;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

        public double calculatePrice() {
            double min = 2.00;
            double max = 100.00;
            return min + (Math.random() * (max - min));
        }
}
