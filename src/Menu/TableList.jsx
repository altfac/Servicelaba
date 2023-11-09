import React, { Component } from 'react';
import axios from 'axios';
import AudioService from '../services/AudioService';
import Player from "../Player";
import './TableList.css';
class TableList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            songs: [],
            tracks: [],
        };
        this.fileInputRef = React.createRef();
    }

    handleFileUpload = (event) => {
        this.selectedFile = event.target.files[0];
    };

    uploadFile = () => {
        if (!this.selectedFile) {
            alert("Файл не выбран");
            return;
        }
        const formData = new FormData();
        formData.append("audio", this.selectedFile);
        const fileName = this.selectedFile.name.slice(0, this.selectedFile.name.lastIndexOf("."));
        if (this.state.tracks.some((track) => track.title === fileName)) {
            alert("Файл с таким именем уже существует");
            return;
        }

        axios
            .post("http://localhost:8090/audio/upload", formData)
            .then((res) => {
                console.log(res.data);
                const file = res.data;
                const fileName = file.slice(0, file.lastIndexOf("."));
                const newTrack = {
                    url: `http://localhost:8090/audio/${res.data}`,
                    title: fileName,
                    tags: ["test"],
                };
                this.setState((prevState) => ({
                    tracks: [...prevState.tracks, newTrack],
                }));
            })
            .catch((error) => {
                console.log(error);
            });
    };

    componentDidMount() {
        AudioService.getSongs().then((res) => {
            const tracks = res.data.map((song) => ({
                url: `http://localhost:8090/audio/${song}`,
                title: song.split(".")[0],
                tags: ["test"],
            }));
            this.setState({ tracks });
        });
    }

    render() {
        const { tracks } = this.state;
        return (
            <div>
                <h2 className="hhh">Audio Player</h2>
                <input type="file" className="fileChoose" onChange={this.handleFileUpload} />
                <button className="file" onClick={this.uploadFile}>Upload</button>
                {tracks.length > 0 && (
                    <Player trackList={tracks} />
                )}
            </div>
        );
    }
}

export default TableList;