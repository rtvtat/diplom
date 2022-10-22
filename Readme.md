#### Дипломный проект Автоматизация тестирования


Настройка окруженияя
1. установка docker и docker-compose
1. убедитесь, что порты 8080, 9999 и 5436 или 3306 свободны


Инструкции по установке
1. скачать исходники
1. запустить контейнеры командой 
    для случая, когда проверяем на бд postgresql:
        
        docker-compose -f docker-compose-postgresql.yml up -d --force-recreate
    остановить контейнеры можно командой
    
        docker-compose -f docker-compose-postgresql.yml down
    для случая, когда проверяем на бд mysql
    
        docker-compose -f docker-compose-mysql.yml up -d --force-recreate
    остановить контейнеры можно командой
    
        docker-compose -f docker-compose-mysql.yml down
        
    логи сервисов можно посмотреть
    
        docker-compose logs

1. Запуск тестов производится командой:

        gradlew clean test allureReport

    запуск в headless-режиме (без открытия браузера
    
        gradlew clean test allureReport -Dheadless=true

1. Просмотра отчета Allure в осуществляется командой

        gradlew allureServe

    