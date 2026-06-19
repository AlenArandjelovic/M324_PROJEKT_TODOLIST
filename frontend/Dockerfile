# Build stage: install dependencies and build the React app with Vite
FROM node:24-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm install
COPY . ./
RUN npm run build

# Runtime stage: serve the static build with Apache HTTPD
FROM httpd:2.4-alpine
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/
EXPOSE 80
CMD ["httpd-foreground"]
