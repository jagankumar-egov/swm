FROM node:4-onbuild
MAINTAINER Arpit Narechania

EXPOSE 3005

COPY index.js index.js

COPY package.json package.json
COPY package-lock.json package-lock.json

RUN ["npm", "install"]
CMD ["npm", "start"]
