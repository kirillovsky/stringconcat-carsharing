# stringconcat-carsharing
Курсовой проект по курсу "Разработка и эксплуатация Enterprise-приложений на Java и Kotlin без боли и сожалений" stringconcat

## Onboarding
### Описание
	https://howto.stringconcat.com/project-carsharing
	
### Event storming
https://miro.com/app/board/o9J_lA2w404=/

### Архитектурные решения
Система включает в себя приложения с разными [арх. характеристиками](https://miro.com/app/board/o9J_lFr3Hp4=/):
* Программу учета автомобилей и связанными с ними документами
* Автоматический учет пробега и проведения ТО
* Мобильное приложение для наших клиентов
* Аналитическую систему для прогнозирования и оптимизации

Так как подсистемы имеют разные арх. характеристики в будущем предпологается
перейти к распределенной архитектуре. Однако, сейчас лучшим решением будет
монолит с луковой архитектурой с разделением на модули по Bounded Context-ам. 
Так как нужно как можно быстрее запуститься, а потом получив прибыль вложиться
в распределенность.
	
### Модель ветвления
В качестве модели ветвления используется [GitHub Flow](https://guides.github.com/introduction/flow/)

### Модули проекта:
* [mainApp](mainApp) - основной модуль

### CI/CD
* В качестве CI-сервера используется GitHub Actions. Наши [Workflows](https://github.com/kirillovsky/stringconcat-carsharing/actions).
* [Workflows-таски](/.github/workflows)

### Среды
- Проект разворачивается в ECS Fargate. [Таски для деплоя на среды](/deploy)
- Ссылки на среды:
    - [Production](https://console.aws.amazon.com/ecs/home?region=us-east-2#/clusters/carsharing)

### Настройка среды разработки
У нас в проекте используется стат. анализатор [detekt](https://detekt.github.io/detekt/).
Поэтому, чтобы не огребать сюрпризов на сборке/запуске лучше установить в IDEA [соответствующий плагин](https://plugins.jetbrains.com/plugin/10761-detekt)
и указать в нем наш [detekt-config](/detekt/detekt-config.yml). Сделать это можно тут `File | Settings | Tools | detekt`

### Полезные скрипты и команды
* Локально запустить проект можно так:
```shell
./runAllLocally.sh
```
* Список зависимостей, которые можно обновить, можно получить так:
```shell
./gradlew dependencyUpdates
```
или так
```shell
./showDependencyUpdates.sh
```
* Установка git hooks:
```shell
./gradlew installGitHooks
```
или так
```shell
./installGitHooks.sh
```
* Отключение git hooks
Обычно git-команды можно выполнять минуя hooks с помощью флага `--no-verify`. Например, 
```shell
git push --no-verify
```
* Удаление git hooks:
```shell
./gradlew removeGitHooks
```
или так
```shell
./removeGitHooks.sh
```
