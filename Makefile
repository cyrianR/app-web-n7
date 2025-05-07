# VARIABLES
COLOR_RESET= \033[0m
COLOR_INFO= \033[32m
COLOR_COMMENT= \033[33m
FRONTEND_DIR = vue-app
BACKEND_DIR = spring-boot-api

# TARGETS
default: help

help: ## Display this help message
	@printf "${COLOR_COMMENT}Usage:${COLOR_RESET}\n"
	@printf " make [target]\n\n"
	@printf "${COLOR_COMMENT}Available targets:${COLOR_RESET}\n"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; { printf " ${COLOR_INFO}%-30s${COLOR_RESET} %s\n", $$1, $$2 }'

.PHONY: dev
dev: stop copy-env ## Development environment, launch hotreloaded preview of the entire project
	@echo "Development database container starting..."
	@docker-compose up -d postgres-dev > /dev/null 2>&1
	@echo "Gradle continuous build starting..."
	@cd $(BACKEND_DIR) && ./gradlew build --continuous > ../logs/gradle-build-logs.txt 2>&1 &
	@echo "API starting..."
	@cd $(BACKEND_DIR) && ./gradlew bootRun > ../logs/gradle-bootRun-logs.txt 2>&1 &
	@echo "Vue application starting..."
	@cd $(FRONTEND_DIR) && npm install > /dev/null 2>&1
	@cd $(FRONTEND_DIR) && npm run dev > ../logs/npm-run-dev-logs.txt 2>&1 &

.PHONY: dev-verbose-api
dev-verbose-api: stop copy-env ## Development environment with verbose api, launch hot-reloaded preview of the entire project
	@echo "Development database container starting..."
	@docker-compose up -d postgres-dev > /dev/null 2>&1
	@echo "Gradle continuous build starting..."
	@cd $(BACKEND_DIR) && ./gradlew build --continuous > ../logs/gradle-build-logs.txt 2>&1 &
	@echo "Vue application starting..."
	@cd $(FRONTEND_DIR) && npm install > /dev/null 2>&1
	@cd $(FRONTEND_DIR) && npm run dev > ../logs/npm-run-dev-logs.txt 2>&1 &
	@echo "API starting..."
	@cd $(BACKEND_DIR) && ./gradlew bootRun

.PHONY: stop
stop: ## Stop development environment
	@echo "Stopping development environment..."
	@test -f .env || cp .env.example .env
	@cd $(BACKEND_DIR) && ./gradlew --stop > /dev/null 2>&1
	@-pkill -f vite
	@docker-compose down > /dev/null 2>&1

.PHONY: clean
clean: stop ## Removes add build artifacts and downloaded dependencies
	@echo "Cleaning API build artifacts..."
	@cd $(BACKEND_DIR) && ./gradlew clean > /dev/null 2>&1 &
	@echo "Cleaning vue application build artifacts..."
	@cd $(FRONTEND_DIR) && rm -rf node_modules && npm cache clean --force > /dev/null 2>&1 &

.PHONY: exec-sql
exec-sql: ## Execute sql commands directly in the development database container, usage : make exec-sql SQL="<sql command>"
	@docker exec -it postgres-dev psql -U dev -d app-web-n7-db -c "$(SQL)"

.PHONY: copy-env
copy-env: ## Create .env file from .env.example and copy .env file in app and api
	@cp .env.example .env
	@cp .env ./$(FRONTEND_DIR)/.env
	@cp .env ./$(BACKEND_DIR)/.env


# TODO
# ajouter commande pour clean la db et remettre juste les données de test
# autres commandes : build, deploy, format, test
# prod scripts