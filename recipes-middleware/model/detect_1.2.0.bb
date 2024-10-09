DESCRIPTION = "Maivin Detection Service"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e153ccee5db0d7cbd514bc6ba454f981"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "\
    https://maivin.deepviewml.com/services/detect/maivin-detect-${PV};name=detect \
    https://github.com/DeepViewML/peopledetect/releases/download/1.0/peopledetect.rtm;name=peopledetect \
    https://github.com/DeepViewML/facedetect/releases/download/1.0/facedetect.rtm;name=facedetect \
    file://detect.service \
    file://detect.default \
    file://LICENSE \
"
SRC_URI[detect.sha256sum] = "5cd0fbfe3ca6ea46fc44135ef36a7a17ed4f80e4c8813b4151f67958eea2e982"
SRC_URI[peopledetect.sha256sum] = "d80c410d54eb33a83df8ac7bfd5d3bca5ba321bb5ac02c318d3817b6d5726b3d"
SRC_URI[facedetect.sha256sum] = "374b081671c42f2d4b73ed6fe71e46bfaa73ec122a6b0c532310afd367a53a82"

DEPENDS = "vaal deepview-rt"
RDEPENDS_${PN} = "libvaal libdeepview-rt"

S = "${WORKDIR}"

inherit features_check systemd

do_install:append () {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/detect.service ${D}${systemd_system_unitdir}

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/detect.default ${D}${sysconfdir}/default/detect

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/maivin-detect-${PV} ${D}${bindir}/detect

    install -d ${D}${datadir}/detect
    install -m 0644 ${WORKDIR}/peopledetect.rtm ${D}${datadir}/detect
    install -m 0644 ${WORKDIR}/facedetect.rtm ${D}${datadir}/detect
}

REQUIRED_DISTRO_FEATURES = "systemd"
SYSTEMD_SERVICE:${PN} = "detect.service"
SYSTEMD_AUTO_ENABLE = "enable"

INSANE_SKIP:${PN} += "already-stripped"

FILES:${PN} += "${systemd_system_unitdir}"
FILES:${PN} += "${sysconfdir}"
FILES:${PN} += "${bindir}"
FILES:${PN} += "${datadir}"
