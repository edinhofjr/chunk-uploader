export class ChunkUploader {
    private chunkSize: number = 10 * 1024 * 1024; // 10MB
    private file: File | undefined;
    private currentChunk: number;
    private totalChunks: number = 0;
    private id: string | undefined ;

    constructor() {
        this.currentChunk = 0;
    }

    async upload(file: File) {
        this.file = file;
        this.totalChunks = Math.ceil(file.size / this.chunkSize);
        this.currentChunk = 0;

        if (!this.id) {
            await this.getId();
        }

        console.log(`Total chunks: ${this.totalChunks}`);
        this.uploadNextChunk();
    }

    private async getId() {
        try {
            const response = await fetch("http://localhost:8080/api/upload-id", { method: "GET" });
            if (response.ok) {
                this.id = await response.text();
                console.log(`Upload ID: ${this.id}`);
            }
        } catch (error) {
            console.error("Erro ao obter upload ID", error);
        }
    }

    private async uploadNextChunk() {
        if (this.currentChunk >= this.totalChunks) {
            console.log("Upload concluído.");
            this.finalizeUpload();
            return;
        }

        const start = this.currentChunk * this.chunkSize;
        const end = Math.min(start + this.chunkSize, this.file.size);
        const chunk = this.file.slice(start, end);

        console.log(`Uploading chunk ${this.currentChunk + 1} of ${this.totalChunks}`);

        try {
            await this.fetchChunk(chunk);
            this.currentChunk++;
            this.uploadNextChunk(); // Chama recursivamente o próximo chunk
        } catch (err) {
            console.error('Erro no upload do chunk', this.currentChunk + 1, err);
        }
    }

    private async fetchChunk(chunk: Blob) {
        try {
            const response = await fetch("http://localhost:8080/api/upload", {
                method: "POST",
                headers: {
                    "Content-Type": "application/octet-stream",
                    "Chunk-Index": this.currentChunk.toString(),
                    "Chunk-Total": this.totalChunks.toString(),
                    "Upload-ID": this.id
                },
                body: chunk
            });

            if (response.ok) {
                console.log(`Chunk ${this.currentChunk + 1} enviado com sucesso.`);
            } else {
                console.error(`Erro no upload do chunk ${this.currentChunk + 1}`);
            }
        } catch (error) {
            console.error("Erro ao enviar chunk", error);
            throw error;
        }
    }

    private async finalizeUpload() {
        try {
            const response = await fetch("http://localhost:8080/api/upload-finalize", {
                method: "POST",
                headers: {
                    "Upload-ID": this.id
                }
            });

            if (response.ok) {
                console.log("Upload finalizado com sucesso.");
            } else {
                console.error("Erro ao finalizar upload.");
            }
        } catch (error) {
            console.error("Erro ao finalizar upload", error);
        }
    }
}
