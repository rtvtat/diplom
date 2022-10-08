FROM node:erbium-alpine3.12 as gate-simulator
WORKDIR /opt/app
COPY . .
RUN npm install
CMD ["npm", "start"]
EXPOSE 9999