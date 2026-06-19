# Build stage: install dependencies and build the React app with Vite
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
COPY .npmrc .
RUN npm ci
COPY . ./
RUN npm run build

# Runtime stage: serve the static build with Apache HTTPD
FROM httpd:2.4-alpine
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/
EXPOSE 80
CMD ["httpd-foreground"]
