# ets-brockerx

## Préparation de la VM pour BrockerX

Ce guide décrit les étapes pour configurer une nouvelle VM afin de lancer le
pipeline CI/CD du projet **BrockerX**.

---

## 1. Mettre à jour la VM

```bash
sudo apt update
sudo apt upgrade -y
```

## 2. Installer Java JDK17

```
sudo apt install -y openjdk-17-jdk
```

Verifier l'installation ou modifier le path

```
java -version
javac -version
```

```
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

## 3. Installer Maven

```
sudo apt install -y maven
```

## 4. Installer Docker

```
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker
```

```
sudo usermod -aG docker $USER
newgrp docker
```

# Self-Hosted

Ensuite pour le self hosted
il suffit de suive les directives sur Github dans les github actions dans la
section runners

## Download

```
$ mkdir actions-runner && cd actions-runner
$ curl -o actions-runner-linux-x64-2.328.0.tar.gz -L https://github.com/actions/runner/releases/download/v2.328.0/actions-runner-linux-x64-2.328.0.tar.gz
$ echo "01066fad3a2893e63e6ca880ae3a1fad5bf9329d60e77ee15f2b97c148c3cd4e  actions-runner-linux-x64-2.328.0.tar.gz" | shasum -a 256 -c
$ tar xzf ./actions-runner-linux-x64-2.328.0.tar.gz
```

et ensuite selon votre repertoir et token pour ca besoin de github

```
$ ./config.sh --url {https://github.com/exemple/ets-brockerx} --token 
{tokenExemple}
$ ./run.sh
```

