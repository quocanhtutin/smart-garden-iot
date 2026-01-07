import React, { useState, useEffect, useRef } from "react";
import { Container, Row, Col, Card, Badge, Spinner, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { io } from "socket.io-client";
import { 
    fetchGardens, 
    fetchRealtimeGarden, 
    fetchSensorLatest,
    fetchIrrigationLog // Giả định bạn đã export hàm này từ UserServices
} from "../../services/UserServices";
import "./Dashboard.scss";

const Dashboard = ({ isSidebarOpen }) => {
    const [gardens, setGardens] = useState([]);
    const [gardenDetails, setGardenDetails] = useState({});
    const [irrigationLogs, setIrrigationLogs] = useState([]); // Lưu trữ nhật ký tưới
    const [loading, setLoading] = useState(true);
    const socketRef = useRef(null);
    const navigate = useNavigate();

    // 1. Khởi tạo dữ liệu
    const initDashboard = async () => {
            try {
                const res = await fetchGardens();
                const list = res.data?.data || res.data || [];
                setGardens(list);
                
                if (list.length > 0) {
                    // Lấy dữ liệu cảm biến và nhật ký tưới cho vườn đầu tiên (hoặc tất cả)
                    const firstGardenId = list[0].id;
                    fetchInitialDetail(firstGardenId);
                    fetchIrrigationData(firstGardenId);
                }
            } catch (e) {
                console.error("Error loading gardens", e);
            } finally {
                setLoading(false);
            }
        };

    useEffect(() => {
        initDashboard();
    }, []);

    // 2. Thiết lập WebSocket lắng nghe thêm sự kiện tưới tiêu
    // useEffect(() => {
    //     socketRef.current = io("http://localhost:3000", { transports: ["websocket"] });

    //     socketRef.current.on("server_send_sensor_data", (data) => {
    //         updateGardenState(data.gardenId, { sensors: data });
    //     });

    //     socketRef.current.on("server_send_device_status", (data) => {
    //         updateGardenState(data.gardenId, { status: data });
    //     });

    //     // Lắng nghe sự kiện tưới tiêu để cập nhật bảng Log ngay lập tức
    //     socketRef.current.on("irrigation_event", (data) => {
    //         console.log("Irrigation event:", data);
    //         // Tải lại nhật ký khi có sự kiện thay đổi (start/end)
    //         fetchIrrigationData(data.gardenId);
    //     });

    //     return () => {
    //         if (socketRef.current) socketRef.current.disconnect();
    //     };
    // }, []);
    useEffect(() => {
        const socket = io("http://localhost:3000", { 
            transports: ["websocket", "polling"] 
        });

        const currentGardens = gardens; // Lưu bản sao để dùng trong callback

        socket.on("connect", () => {
            console.log("Dashboard connected:", socket.id);
            currentGardens.forEach(g => {
                socket.emit("garden:join", { gardenId: Number(g.id) });
            });
        });

        // Lắng nghe MỌI sự kiện từ Server
        socket.onAny((eventName, payload) => {
            console.log(`🔍 [Socket Event]: ${eventName}`, payload);

            // Nếu có bất kỳ sự thay đổi nào về thiết bị hoặc tưới tiêu, load lại Log cho vườn đó
            if (eventName === "device:status" || eventName === "irrigation:update") {
                if (payload.gardenId) {
                    console.log("💧 Phát hiện thay đổi trạng thái, đang cập nhật nhật ký...");
                    //fetchIrrigationData(payload.gardenId);
                    initDashboard();
                }
            }
        });

        // Các listener cụ thể để cập nhật State nhanh (không cần load lại API)
        socket.on("sensor:update", (data) => {
            updateGardenState(data.gardenId, { sensors: data });
        });

        socket.on("device:status", (data) => {
            updateGardenState(data.gardenId, { status: data });
        });

        socketRef.current = socket;

        return () => {
            if (socketRef.current) socketRef.current.disconnect();
        };
    }, [gardens]); // Quan trọng: Re-run khi gardens list được tải xong để join room

    const fetchInitialDetail = async (id) => {
        try {
            const [statusRes, sensorRes] = await Promise.all([
                fetchRealtimeGarden(id),
                fetchSensorLatest(id)
            ]);
            updateGardenState(id, {
                status: statusRes.data?.data || statusRes.data,
                sensors: sensorRes.data?.data || sensorRes.data
            });
        } catch (e) {
            console.error(`Error initial fetch for garden ${id}`);
        }
    };

    // Hàm lấy nhật ký tưới tiêu
    const fetchIrrigationData = async (id) => {
        try {
            const res = await fetchIrrigationLog(id);
            const logs = res.data?.data || res.data || [];
            setIrrigationLogs(logs.slice(0, 5)); // Chỉ lấy 5 bản ghi mới nhất cho Dashboard
        } catch (e) {
            console.error("Error fetching irrigation logs", e);
        }
    };

    const updateGardenState = (gardenId, newData) => {
        setGardenDetails(prev => ({
            ...prev,
            [gardenId]: { ...prev[gardenId], ...newData }
        }));
    };

    if (loading) return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <Spinner animation="border" variant="success" />
        </div>
    );

    return (
        <div className={`dashboard-wrapper ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
            <Container className="py-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 className="fw-bold">My Smart Gardens</h2>
                    </div>
                    <Badge bg="info" className="p-2">Live Connection: Active</Badge>
                </div>

                {/* Phần 1: Các Card thông số vườn */}
                <Row className="g-4 mb-5">
                    {gardens.map((garden) => {
                        const detail = gardenDetails[garden.id];
                        const sensor = detail?.sensors;
                        const status = detail?.status;

                        return (
                            <Col key={garden.id} xl={4} md={6}>
                                <Card className="garden-card border-0 shadow-sm h-100 shadow-hover" 
                                      onClick={() => navigate(`/sensors/${garden.id}`)}
                                      style={{ cursor: 'pointer', transition: '0.3s' }}>
                                    <Card.Body className="p-4">
                                        <div className="d-flex justify-content-between align-items-center mb-3">
                                            <h5 className="fw-bold m-0">{garden.gardenName}</h5>
                                            <Badge bg={status?.isConnected ? "success" : "secondary"}>
                                                {status?.isConnected ? "Online" : "Offline"}
                                            </Badge>
                                        </div>

                                        <div className="sensor-brief d-flex justify-content-between bg-light rounded p-3 mb-3">
                                            <div className="text-center">
                                                <div className="small text-muted">Temp</div>
                                                <div className="fw-bold text-danger">{sensor?.temperature ?? "--"}°C</div>
                                            </div>
                                            <div className="text-center">
                                                <div className="small text-muted">Air Humid</div>
                                                <div className="fw-bold text-primary">{sensor?.airHumidity ?? "--"}%</div>
                                            </div>
                                            <div className="text-center">
                                                <div className="small text-muted">Soil</div>
                                                <div className="fw-bold text-success">{sensor?.soilMoisture ?? "--"}%</div>
                                            </div>
                                        </div>

                                        <div className="device-status d-flex gap-3">
                                            {/* Hiển thị trạng thái tưới trực tiếp trên card */}
                                            <div className={`status-tag ${status?.isPumpOn ? 'active' : ''}`}>
                                                <i className={`fa fa-tint me-1 ${status?.isPumpOn ? 'fa-spin' : ''}`}></i> 
                                                Pump: {status?.pumpStatus ? 'WATERING' : 'OFF'}
                                            </div>
                                            <div className={`status-tag ${status?.isLedOn ? 'active' : ''}`}>
                                                <i className="fa fa-sun-o me-1"></i> LED: {status?.isLedOn ? 'ON' : 'OFF'}
                                            </div>
                                        </div>
                                    </Card.Body>
                                </Card>
                            </Col>
                        );
                    })}
                </Row>

                {/* Phần 2: Bảng Nhật ký tưới tiêu gần đây */}
                <h4 className="fw-bold mb-3">Recent Irrigation Logs</h4>
                <Card className="border-0 shadow-sm rounded-4 overflow-hidden">
                    <Table hover responsive className="align-middle mb-0">
                        <thead className="bg-light">
                            <tr className="text-muted small">
                                <th className="ps-4">START TIME</th>
                                <th>MODE</th>
                                <th>DURATION</th>
                                <th>STATUS</th>
                                <th className="pe-4">NOTES</th>
                            </tr>
                        </thead>
                        <tbody>
                            {irrigationLogs.length > 0 ? (
                                irrigationLogs.map((log) => (
                                    <tr key={log.id}>
                                        <td className="ps-4">
                                            <div className="fw-bold">{new Date(log.startTime).toLocaleTimeString()}</div>
                                            <div className="text-muted x-small">{new Date(log.startTime).toLocaleDateString()}</div>
                                        </td>
                                        <td>
                                            <Badge bg="soft-primary" className="text-primary border border-primary">
                                                {log.mode}
                                            </Badge>
                                        </td>
                                        <td>{log.duration ? `${log.duration}s` : "---"}</td>
                                        <td>
                                            <span className={`fw-bold ${log.status === 'completed' ? 'text-success' : 'text-warning'}`}>
                                                ● {log.status.toUpperCase()}
                                            </span>
                                        </td>
                                        <td className="text-muted pe-4 small">{log.note || "System Auto"}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5" className="text-center py-4 text-muted">No irrigation records found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card>
            </Container>
        </div>
    );
};

export default Dashboard;