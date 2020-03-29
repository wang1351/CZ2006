import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import ButtonBase from "@material-ui/core/ButtonBase";
import Typography from "@material-ui/core/Typography";
import NavBar from "../Components/NavBar";
import { ListItem, Grid, Paper, Button } from "@material-ui/core";

const images = [
  {
    url: "https://source.unsplash.com/random",
    title: "District 1",
    width: "100%"
  },
  {
    url: "https://source.unsplash.com/random",
    title: "District 2",
    width: "100%"
  },
  {
    url: "/static/images/grid-list/camera.jpg",
    title: "District 3",
    width: "100%"
  },
  {
    url: "/static/images/grid-list/breakfast.jpg",
    title: "District 4",
    width: "100%"
  },
  {
    url: "/static/images/grid-list/breakfast.jpg",
    title: "District 5",
    width: "100%"
  },
  {
    url: "/static/images/grid-list/breakfast.jpg",
    title: "District 6",
    width: "100%"
  }
];

const useStyles = makeStyles(theme => ({
  root: {
    display: "flex",
    flexWrap: "wrap"
  },
  image: {
    position: "relative",
    height: 200,
    [theme.breakpoints.down("xs")]: {
      width: "100% !important", // Overrides inline-style
      height: 100
    },
    "&:hover, &$focusVisible": {
      zIndex: 1,
      "& $imageBackdrop": {
        opacity: 0.15
      },
      "& $imageMarked": {
        opacity: 0
      },
      "& $imageTitle": {
        border: "4px solid currentColor"
      }
    }
  },
  focusVisible: {},
  imageButton: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    color: theme.palette.common.white
  },
  imageSrc: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    backgroundSize: "cover",
    backgroundPosition: "center 40%"
  },
  imageBackdrop: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    backgroundColor: theme.palette.common.black,
    opacity: 0.4,
    transition: theme.transitions.create("opacity")
  },
  imageTitle: {
    position: "relative",
    padding: `${theme.spacing(2)}px ${theme.spacing(4)}px ${theme.spacing(1) +
      6}px`
  },
  imageMarked: {
    height: 3,
    width: 18,
    backgroundColor: theme.palette.common.white,
    position: "absolute",
    bottom: -2,
    left: "calc(50% - 9px)",
    transition: theme.transitions.create("opacity")
  }
}));

export default function AreaListUI() {
  const classes = useStyles();

  return (
    <div>
      <div>
        <NavBar />
      </div>
      <Grid container>
        <Grid item xs={3}>
          <div className={classes.root}>
            <Typography variant="h3" style={{ color: "white" }}>
              Results
            </Typography>

            {images.map(image => (
              <ListItem>
                <ButtonBase
                  focusRipple
                  key={image.title}
                  className={classes.image}
                  focusVisibleClassName={classes.focusVisible}
                  style={{
                    width: image.width
                  }}
                >
                  <span
                    className={classes.imageSrc}
                    style={{
                      backgroundImage: `url(${image.url})`
                    }}
                  />
                  <span className={classes.imageBackdrop} />
                  <span className={classes.imageButton}>
                    <Typography
                      component="span"
                      variant="subtitle1"
                      color="inherit"
                      className={classes.imageTitle}
                    >
                      {image.title}
                      <span className={classes.imageMarked} />
                    </Typography>
                  </span>
                </ButtonBase>
              </ListItem>
            ))}
          </div>
        </Grid>
        <Grid item xs={9}>
          <Paper style={{ padding: 20, marginTop: 10, marginBotton: 10 }}>
            <h1>MAP</h1>
            <h1>//</h1>
            <h1>//假装有地图</h1>
            <h1>//</h1>
            <h1>//</h1>
            <div>
              <Button
                variant="outlined"
                size="large"
                color="primary"
                className={classes.margin}
              >
                A
              </Button>
              <Button
                variant="outlined"
                size="large"
                color="primary"
                className={classes.margin}
              >
                B
              </Button>
              <Button
                variant="outlined"
                size="large"
                color="primary"
                className={classes.margin}
              >
                C
              </Button>
              <Button
                variant="outlined"
                size="large"
                color="primary"
                className={classes.margin}
              >
                D
              </Button>
            </div>
            <Button
              variant="contained"
              size="large"
              color="primary"
              className={classes.margin}
              href="/house@area"
            >
              See houses in this area
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </div>
  );
}
