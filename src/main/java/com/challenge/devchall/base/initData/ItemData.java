package com.challenge.devchall.base.initData;

import com.challenge.devchall.item.service.ItemService;
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
                itemService.create("FCE411","font","FCE411", "000000",150);
                itemService.create("basic","font","3D4451", "FFFFFF",0);
                itemService.create("ED3096","font","ED3096", "FFFFFF",300);
                itemService.create("FF9900","font","FF9900", "000000",200);
                itemService.create("1144FC","font","1144FC", "FFFFFF",150);
                itemService.create("3CB24F","font","3CB24F", "FFFFFF",150);

            }
        };
    }
}
