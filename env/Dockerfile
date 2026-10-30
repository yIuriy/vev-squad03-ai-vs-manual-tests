# Ambiente reprodutível do experimento: Ubuntu 22.04 + Java 11 + Defects4J v3.0.1
# Build:  docker build -t vev-d4j env
# Uso:    docker run --rm -it -v "${PWD}:/work" vev-d4j
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive \
    TZ=America/Los_Angeles \
    LANG=C.UTF-8 \
    D4J_HOME=/opt/defects4j

RUN apt-get update && apt-get install -y --no-install-recommends \
        openjdk-11-jdk-headless git subversion perl cpanminus make \
        build-essential curl unzip ca-certificates tzdata \
    && ln -fs /usr/share/zoneinfo/$TZ /etc/localtime \
    && rm -rf /var/lib/apt/lists/*

# Versão fixada para que todos os membros usem exatamente o mesmo Defects4J
RUN git clone --depth 1 --branch v3.0.1 https://github.com/rjust/defects4j.git $D4J_HOME \
    && cd $D4J_HOME \
    && cpanm --installdeps --notest . \
    && ./init.sh

ENV PATH=$D4J_HOME/framework/bin:$PATH

WORKDIR /work
CMD ["bash"]
