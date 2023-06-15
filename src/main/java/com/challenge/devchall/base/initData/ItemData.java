package com.challenge.devchall.base.initData;

import com.challenge.devchall.item.service.ItemService;
import com.challenge.devchall.photo.service.PhotoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"prod"})
public class ItemData {
    @Bean
    CommandLineRunner initData(
            ItemService itemService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run (String... args) throws Exception {
                itemService.create("L-F-FCE411","font","FCE411", "000000",300);
                itemService.create("basic","font","3D4451", "FFFFFF",0);
                itemService.create("ED3096","font","ED3096", "FFFFFF",0);
                itemService.create("FF9900","font","FF9900", "000000",0);
                itemService.create("1144FC","font","1144FC", "FFFFFF",0);
                itemService.create("3CB24F","font","3CB24F", "FFFFFF",0);
            }
        };
    }
}
