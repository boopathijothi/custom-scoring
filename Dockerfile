FROM opensearchproject/opensearch:latest

# Copy your plugin
COPY build/distributions/customscoring.zip /tmp/customscoring.zip

RUN /usr/share/opensearch/bin/opensearch-plugin install --batch file:///tmp/customscoring.zip


