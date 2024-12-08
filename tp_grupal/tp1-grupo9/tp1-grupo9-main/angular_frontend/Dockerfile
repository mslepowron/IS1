# Usa una imagen base con Node.js
FROM node:18-alpine

# Establece el directorio de trabajo
WORKDIR /usr/src/app

# Copia los archivos del proyecto
COPY package*.json ./

RUN npm install -g @angular/cli

# Instala las dependencias de Node.js
RUN npm install

# Copia el resto del proyecto
COPY . .

# Exponer el puerto en el que Angular se ejecutará
EXPOSE 4200

# Comando para levantar la aplicación
CMD ["ng", "serve", "--host", "0.0.0.0", "--proxy-config", "proxy.conf.json"]


