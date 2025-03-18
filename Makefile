# VARIABLES
COLOR_RESET= \033[0m
COLOR_INFO= \033[32m
COLOR_COMMENT= \033[33m

# TARGETS
default: help

help: ## Display this help message
	@printf "${COLOR_COMMENT}Usage:${COLOR_RESET}\n"
	@printf " make [target]\n\n"
	@printf "${COLOR_COMMENT}Available targets:${COLOR_RESET}\n"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; { printf " ${COLOR_INFO}%-30s${COLOR_RESET} %s\n", $$1, $$2 }'

.PHONY: dev
dev: stop ## Development environment, launch hotreloaded preview of the entire project
	@echo "Development database container starting..."
	@docker compose up -d postgres-dev > /dev/null 2>&1
	@echo "Gradle continuous build starting..."
	@cd spring-boot-api && ./gradlew build --continuous > ../logs/gradle-build-logs.txt 2>&1 &
	@echo "API starting..."
	@cd spring-boot-api && ./gradlew bootRun > ../logs/gradle-bootRun-logs.txt 2>&1 &

.PHONY: dev-verbose-api
dev-verbose-api: stop ## Development environment with verbose api, launch hotreloaded preview of the entire project
	@echo "Development database container starting..."
	@docker compose up -d postgres-dev > /dev/null 2>&1
	@echo "Gradle continuous build starting..."
	@cd spring-boot-api && ./gradlew build --continuous > ../logs/gradle-build-logs.txt 2>&1 &
	@echo "API starting..."
	@cd spring-boot-api && ./gradlew bootRun

.PHONY: stop
stop: ## Stop development environment
	@echo "Stopping development environment..."
	@cd spring-boot-api && ./gradlew --stop > /dev/null 2>&1 &
	@docker compose down postgres-dev > /dev/null 2>&1

.PHONY: clean
clean: ## Removes add build artifacts and downloaded dependencies
	@echo "Cleaning API build artifacts..."
	@cd spring-boot-api && ./gradlew clean > /dev/null 2>&1 &

.PHONY: exec-sql
exec-sql: ## Execute sql commands directly in the development database container
	@docker exec -it postgres-dev psql -U dev -d app-web-n7-db -c "$(SQL)"

# TODO
# ajouter commande pour clean la db et remettre juste les données de test
# dans "clean", supprimer tout le volume du container de la db 
# autres commandes : build, deploy, format, test
# prod scripts