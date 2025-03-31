import { useEffect, useRef, useState } from "react";
import { ChunkUploader } from "./ChunkUploader";

export default () => {
    const [file, setFile] = useState<File>();
    const [uploader, setUploader] = useState<ChunkUploader>(new ChunkUploader());

    useEffect(() => {
        if (!file) return;
        
        uploader.upload(file)
        
    }, [file])

    return <>
        <input type="file" 
        onChange={e => {
            const file: File = e.target.files?.[0] as File;
            if (file) {
                setFile(file);
            }
        }}
        /><button>Upload</button></>
}