import React, { useState, useEffect, useCallback, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col, Card, Button, Table, Spinner, Badge, ProgressBar } from "react-bootstrap";
import { io } from "socket.io-client";
import { 
    fetchGardenById, 
    fetchSensorLatest, 
    fetchSensorLog, 
    turnOnPump, 
    turnOffPump, 
    turnOnLED, 
    turnOffLED 
} from "../../services/UserServices";
import { toast } from "react-toastify";
import "./Sensor.scss";

const Sensor = ({ isSidebarOpen }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const socketRef = useRef(null);
    
    const [garden, setGarden] = useState(null);
    const [latestSensor, setLatestSensor] = useState(null);
    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(false);
    const [pumpDuration, setPumpDuration] = useState(60); // Mặc định 60 giây

    // 1. Tải dữ liệu ban đầu qua API
    const loadInitialData = useCallback(async (showLoading = true) => {
        try {
            if (showLoading) setLoading(true);
            const [gardenRes, latestRes, logsRes] = await Promise.all([
                fetchGardenById(id),
                fetchSensorLatest(id),
                fetchSensorLog(id)
            ]);
            
            setGarden(gardenRes.data?.data || gardenRes.data);
            setLatestSensor(latestRes.data?.data[0]);
            setLogs(logsRes.data?.data || logsRes.data || []);
        } catch (e) {
            toast.error("Failed to sync with garden sensors");
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        loadInitialData();
    }, [loadInitialData]);

    
    useEffect(() => {
        // 1. Khởi tạo socket
        const socket = io("http://localhost:3000", {

            transports: ["websocket", "polling"],

            reconnectionAttempts: 5

        });
        const currentId = Number(id);
        socket.on("connect", () => {
            console.log("Connected to Socket. ID:", socket.id);
            socket.emit("garden:join", { gardenId: currentId });
            console.log(`Đã gửi yêu cầu join vào room: garden_${currentId}`);

        });

        socket.on("sensor:update", (newData) => {

            console.log("Nhận dữ liệu Sensor real-time:", newData);
            setLatestSensor(newData);
            setLogs(prevLogs => {
                const logEntry = {
                    ...newData,
                    recordedAt: newData.timestamp || newData.recordedAt || new Date().toISOString()

                };
                return [logEntry, ...prevLogs].slice(0, 10);

            });
            console.log(" Đã cập nhật UI cho vườn:", currentId);
        });

        socket.on("device:status", (statusData) => {
            console.log("📱 Trạng thái thiết bị thay đổi:", statusData);
            if (Number(statusData.gardenId) === currentId) {

                setLatestSensor(prev => ({ ...prev, ...statusData }));
            }
        });

        socket.onAny((eventName, payload) => {
            if (eventName !== "connect") {
                loadInitialData();
                console.log(`🔍 Server đang phát sự kiện tên là: [${eventName}]`, payload);
            }
        });

        socket.on("connect_error", (err) => console.error("🔴 Socket Error:", err));
        socketRef.current = socket;
        return () => {
            if (socketRef.current) {
                // Rời phòng trước khi ngắt kết nối
                socketRef.current.emit("garden:leave", { gardenId: currentId });
                socketRef.current.disconnect();
            }
        };

    }, [id]);

    // 3. Hàm điều khiển thiết bị
    // const handleDeviceControl = async (device, currentStatus) => {
    //     try {
    //         setActionLoading(true);
    //         const newStatus = !currentStatus;
            
    //         if (device === 'pump') {
    //             newStatus ? await turnOnPump(id) : await turnOffPump(id);
    //         } else {
    //             newStatus ? await turnOnLED(id) : await turnOffLED(id);
    //         }
    //         toast.info(`Command sent to ${device}... waiting for device response`);
    //     } catch (e) {
    //         toast.error(`Control command failed for ${device}`);
    //     } finally {
    //         setActionLoading(false);
    //     }
    // };
    const handleDeviceControl = async (device, currentStatus) => {
        try {
            setActionLoading(true);
            const newStatus = !currentStatus;
            
            if (device === 'pump') {
                if (newStatus) {
                    // Khi BẬT thì gửi kèm thời gian
                    await turnOnPump(id, pumpDuration); 
                    toast.info(`Sending ON command for ${pumpDuration}s...`);
                } else {
                    await turnOffPump(id);
                }
            } else {
                newStatus ? await turnOnLED(id) : await turnOffLED(id);
            }
        } catch (e) {
            toast.error(e.response?.data?.message || `Control command failed`);
        } finally {
            setActionLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center vh-100">
                <div className="text-center">
                    <Spinner animation="grow" variant="primary" />
                    <p className="mt-2 text-muted fw-bold">Connecting to Garden Live Stream...</p>
                </div>
            </div>
        );
    }

    return (
        <div className={`dashboard-wrapper ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
            <Container fluid className="py-4 px-lg-4">
                {/* Header Section */}
                <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4 gap-3">
                    <div className="d-flex align-items-center">
                        <Button variant="white" className="shadow-sm rounded-circle me-3 border" onClick={() => navigate(-1)}>
                            <i className="fa fa-chevron-left"></i>
                        </Button>
                        <div>
                            <h2 className="fw-bold text-dark mb-0">{garden?.gardenName}</h2>
                            <div className="d-flex align-items-center gap-2">
                                <span className="pulse-indicator"></span>
                                <span className="text-muted small fw-medium">
                                    Live monitoring
                                </span>
                            </div>
                        </div>
                    </div>
                    <div className="d-flex gap-2">
                        <Badge bg="light" className="text-dark border d-flex align-items-center px-3 rounded-pill">
                            ID: {id}
                        </Badge>
                        <Button variant="primary" className="rounded-pill px-4 shadow-sm" onClick={() => loadInitialData(true)}>
                            <i className="fa fa-refresh me-2"></i> Full Sync
                        </Button>
                    </div>
                </div>

                {/* Main Stats */}
                <Row className="g-4 mb-4">
                    <SensorMetricCard 
                        icon="thermometer-half" color="danger" label="Temperature" 
                        value={latestSensor?.temperature} unit="°C"
                        subtext="Ambient temp"
                    />
                    <SensorMetricCard 
                        icon="tint" color="primary" label="Air Humidity" 
                        value={latestSensor?.airHumidity} unit="%" 
                        subtext="Atmospheric moisture"
                    />
                    <SensorMetricCard 
                        icon="leaf" color="success" label="Soil Moisture" 
                        value={latestSensor?.soilMoisture} unit="%"
                        subtext={`Auto-water threshold: ${garden?.autoIrrigationThreshold}%`}
                    />
                </Row>

                <Row className="g-4">
                    {/* Control Panel */}
                    <Col lg={4}>
                        <Card className="border-0 shadow-sm rounded-4 h-100 overflow-hidden">
                            <div className="bg-dark p-3 text-white">
                                <h5 className="mb-0 fw-bold"><i className="fa fa-sliders me-2"></i>Live Controls</h5>
                            </div>
                            <Card.Body className="p-4">
                                {/* <ControlSwitch 
                                    label="Water Pump" icon="tint" color="info"
                                    active={latestSensor?.isPumpOn}
                                    loading={actionLoading}
                                    onToggle={() => handleDeviceControl('pump', latestSensor?.isPumpOn)}
                                /> */}
                                <ControlSwitch 
                                    label="Water Pump" icon="tint" color="info"
                                    active={latestSensor?.isPumpOn}
                                    loading={actionLoading}
                                    onToggle={() => handleDeviceControl('pump', latestSensor?.isPumpOn)}
                                    showInput={true} // Chỉ hiện input cho máy bơm
                                    duration={pumpDuration}
                                    setDuration={setPumpDuration}
                                />
                                <hr className="my-4" />
                                <ControlSwitch 
                                    label="Grow Lights" icon="sun-o" color="warning"
                                    active={latestSensor?.isLedOn}
                                    loading={actionLoading}
                                    onToggle={() => handleDeviceControl('led', latestSensor?.isLedOn)}
                                />
                                
                                <div className="mt-4 p-3 bg-light rounded-3 text-center">
                                    <div className="small fw-bold mb-2">Real-time Data Stream</div>
                                    <ProgressBar animated variant="success" now={100} style={{height: '4px'}} />
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>

                    {/* History Table */}
                    <Col lg={8}>
                        <Card className="border-0 shadow-sm rounded-4 h-100">
                            <Card.Header className="bg-white border-0 p-4 pb-0">
                                <h5 className="fw-bold mb-0">Live Sensor Logs</h5>
                            </Card.Header>
                            <Card.Body className="p-4">
                                <div className="table-responsive">
                                    <Table hover borderless className="align-middle">
                                        <thead className="table-light">
                                            <tr className="text-muted small">
                                                <th className="rounded-start">Time</th>
                                                <th>Temp</th>
                                                <th>Air Humid</th>
                                                <th>Soil Humid</th>
                                                <th>Light/Dark</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {logs.slice(0, 10).map((log, idx) => (
                                                <tr key={idx} className="border-bottom">
                                                    <td className="py-3 fw-medium small">
                                                        {new Date(log.recordedAt || Date.now()).toLocaleTimeString()}
                                                    </td>
                                                    <td><span className="fw-bold text-danger">{log.temperature}°C</span></td>
                                                    <td><span className="fw-bold text-primary">{log.airHumidity || log.airHumidity}%</span></td>
                                                    <td><span className="fw-bold text-success">{log.soilMoisture}%</span></td>
                                                    <td>
                                                        <Badge 
                                                            bg={log.isDark ? "dark" : "warning"} 
                                                            className={`text-${log.isDark ? "white" : "dark"} border`}
                                                        >
                                                            {log.isDark ? "Dark" : "Light"}
                                                        </Badge>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </Table>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

// --- Sub-components (Giữ nguyên cấu trúc nhưng sửa props nếu cần) ---
const SensorMetricCard = ({ icon, color, label, value, unit, subtext }) => (
    <Col xl={4} md={6}>
        <Card className="border-0 shadow-sm rounded-4 h-100">
            <Card.Body className="p-4 text-center">
                <div className={`icon-circle bg-soft-${color} text-${color} mb-3 mx-auto`}>
                    <i className={`fa fa-${icon} fs-3`}></i>
                </div>
                <h6 className="text-muted text-uppercase fw-bold small mb-1">{label}</h6>
                <h2 className={`fw-bold text-${color} mb-2`}>
                    {value !== undefined ? `${value}${unit}` : "--"}
                </h2>
                <div className="small text-muted fw-medium">{subtext}</div>
            </Card.Body>
        </Card>
    </Col>
);

// const ControlSwitch = ({ label, icon, active, color, onToggle, loading }) => (
//     <div className="d-flex align-items-center justify-content-between">
//         <div className="d-flex align-items-center">
//             <div className={`p-2 bg-light rounded-3 text-${color} me-3`}>
//                 <i className={`fa fa-${icon} fs-5`}></i>
//             </div>
//             <div>
//                 <div className="fw-bold small">{label}</div>
//                 <div className={`small fw-bold text-${active ? color : 'muted'}`}>
//                     {active ? "ON" : "OFF"}
//                 </div>
//             </div>
//         </div>
//         <Button 
//             variant={active ? color : "outline-secondary"} 
//             className="rounded-pill px-4 btn-sm shadow-sm"
//             onClick={onToggle}
//             disabled={loading}
//         >
//             {loading ? <Spinner size="sm" /> : (active ? "Turn Off" : "Turn On")}
//         </Button>
//     </div>
// );
const ControlSwitch = ({ label, icon, active, color, onToggle, loading, showInput, duration, setDuration }) => (
    <div className="d-flex align-items-center justify-content-between">
        <div className="d-flex align-items-center">
            <div className={`p-2 bg-light rounded-3 text-${color} me-3`}>
                <i className={`fa fa-${icon} fs-5`}></i>
            </div>
            <div>
                <div className="fw-bold small">{label}</div>
                <div className={`small fw-bold text-${active ? color : 'muted'}`}>
                    {active ? "ON" : "OFF"}
                </div>
            </div>
        </div>
        
        <div className="d-flex align-items-center gap-2">
            {/* Nếu là máy bơm và đang tắt, cho phép nhập thời gian */}
            {showInput && !active && (
                <div className="d-flex align-items-center border rounded-pill px-2 bg-white">
                    <input 
                        type="number" 
                        value={duration}
                        onChange={(e) => setDuration(Number(e.target.value))}
                        style={{ width: '50px', border: 'none', textAlign: 'center', outline: 'none', fontSize: '12px' }}
                    />
                    <span className="text-muted small">s</span>
                </div>
            )}
            
            <Button 
                variant={active ? color : "outline-secondary"} 
                className="rounded-pill px-3 btn-sm shadow-sm"
                onClick={onToggle}
                disabled={loading}
            >
                {loading ? <Spinner size="sm" /> : (active ? "Turn Off" : "Turn On")}
            </Button>
        </div>
    </div>
);

export default Sensor;